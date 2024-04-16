from os import environ
from flask import Flask, request, abort
import firebase_admin
from firebase_admin import credentials, firestore
import json
import hashlib
import base64
import logging


app = Flask(__name__)

logging.basicConfig(level=logging.INFO)

# Load your Firebase service account key
cred = credentials.Certificate('src/security/plutusfirebase.json')

# Initialize the Firebase app with the loaded credentials
firebase_app = firebase_admin.initialize_app(cred)

# Create a Firestore client instance associated with the app
db = firestore.client(app=firebase_app)


@app.route('/webhook', methods=['POST'])
def webhook():
    print("Hello")
    # Process the incoming webhook payload
    return 'Webhook received', 200


@app.route('/callback', methods=['POST'])
def payout_callback():
    data = request.get_json()  # Get JSON data sent by POST
    received_sign = data.get('sign')  # Extract the sign from the received data
    if not received_sign:
        abort(400, description="Sign not provided")
    
    # Remove the sign from the data dictionary as it's not part of the data used to generate the sign
    data.pop('sign', None)
    
    # Your API Payment Key
    api_payment_key = environ['API_PAYOUT_KEY']

    # Step 2: Generate the sign using your API payment key
    generated_sign = generate_sign(data, api_payment_key)

    # Step 3: Verify the sign
    if not verify_sign(received_sign, generated_sign):
        abort(400, description="Invalid signature")
    
    # Extract the relevant fields from the callback data
    order_id = data.get('order_id')  # Make sure 'order_id' is the correct field name in your callback data
    network = data.get('network')
    txid = data.get('txid')
    status = data.get('status')
    is_final = data.get('is_final')

    # Prepare the data to be updated in Firestore
    update_data = {
        'network': network,
        'transaction_id': txid,
        'status': status,
        'is_final': is_final
    }
    
    try:
        # Update the user's database with the extracted data
        update_user_database_order(order_id, update_data)
        response_message = 'Callback processed and database updated successfully!'
        return response_message, 200
    except Exception as e:
        print(f"Error updating user database: {e}")
        return f"Error processing callback: {e}", 500


def update_user_database_order(order_id_received, update_data):
    try:
        # Reference to the 'ORDERS' collection
        orders_ref = db.collection('ORDERS')

        # Query for the document with the matching 'payment_reference' equal to the received 'order_id'
        query = orders_ref.where('payment_reference', '==', order_id_received).limit(1)
        docs = query.stream()

        matching_document = None
        for doc in docs:
            matching_document = doc
            break

        if matching_document:
            # Document found, proceed to update
            doc_ref = orders_ref.document(matching_document.id)
            doc_ref.update(update_data)
            print(f"Order with payment_reference {order_id_received} updated successfully.")
        else:
            print(f"No order found with payment_reference: {order_id_received}")
            # Handle the case where no matching document is found
            raise ValueError(f"No order found with payment_reference: {order_id_received}")

    except Exception as e:
        print(f"Error updating Firestore: {e}")
        raise e


def generate_sign(data, api_payment_key):
    try:
        # Convert data dictionary to JSON string
        data_json = json.dumps(data, ensure_ascii=False, separators=(',', ':')).encode('utf-8')
        # Encode data string in base64
        data_base64 = base64.b64encode(data_json)
        # Generate MD5 hash of the base64 encoded data combined with your API payment key
        hash_string = data_base64 + api_payment_key.encode()
        hash = hashlib.md5(hash_string).hexdigest()
        return hash
    except Exception as e:
        raise ValueError(f"Error generating sign: {e}")

def verify_sign(received_sign, generated_sign):
    try:
        # Compare the received sign with the generated sign securely
        return received_sign == generated_sign
    except Exception as e:
        raise ValueError(f"Error verifying sign: {e}")

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5250, debug=True)

