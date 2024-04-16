# Use a base image with Python installed (adjust the version as needed)
FROM python:3.9-slim

# Set the working directory in the container
WORKDIR /jumpforce

# Copy the Python requirements file and install dependencies
COPY requirements.txt /jumpforce/
RUN pip install --no-cache-dir -r requirements.txt

# Gunicorn will be installed as part of the requirements or separately if not included
RUN pip install gunicorn

# Copy the application files into the container
COPY . /jumpforce

# Make the main application file executable (if necessary)
RUN chmod +x /jumpforce/src/cryptomus_webhook.py

# Expose a port - this is just for documentation purposes, as Heroku will ignore this
EXPOSE 8000

# Use gunicorn to serve the Flask app, with port binding dynamically assigned via the $PORT environment variable
CMD [""]
