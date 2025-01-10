/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.optimus.util;

/**
 *
 * @author MalickMoro-Samah
 */
public class EmailContentGenerator {

    public static String generateAccountCreationContent(String username, String otpCode) {
        return String.format("""
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border: 1px solid #dddddd;
                            border-radius: 8px;
                            overflow: hidden;
                        }
                        .header {
                            background-color: #007BFF;
                            color: #ffffff;
                            text-align: center;
                            padding: 20px;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                            line-height: 1.6;
                            color: #333333;
                        }
                        .otp-code {
                            display: block;
                            margin: 20px auto;
                            text-align: center;
                            font-size: 32px;
                            font-weight: bold;
                            color: #007BFF;
                            background-color: #f9f9f9;
                            padding: 10px;
                            border: 1px dashed #007BFF;
                            width: fit-content;
                            border-radius: 8px;
                        }
                        .footer {
                            background-color: #f4f4f4;
                            text-align: center;
                            padding: 10px;
                            font-size: 12px;
                            color: #888888;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            Welcome to The Plutus Home!
                        </div>
                        <div class="content">
                            <p>Hi %s,</p>
                            <p>We are excited to have you on board! To complete your registration, please use the OTP code below:</p>
                            <div class="otp-code">%s</div>
                            <p>If you did not request this, please ignore this email or contact our support team for assistance.</p>
                            <p>Thank you for choosing our service!</p>
                        </div>
                        <div class="footer">
                            &copy; 2024 The Plutus Home. All rights reserved.<br>
                            This is an automated email; please do not reply.
                        </div>
                    </div>
                </body>
                </html>
                """, username, otpCode);
    }

    public static String generateForgotPasswordContent(String username, String otpCode) {
        return String.format("""
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #ffffff;
                        border: 1px solid #dddddd;
                        border-radius: 8px;
                        overflow: hidden;
                    }
                    .header {
                        background-color: #007BFF;
                        color: #ffffff;
                        text-align: center;
                        padding: 20px;
                        font-size: 24px;
                    }
                    .content {
                        padding: 20px;
                        line-height: 1.6;
                        color: #333333;
                    }
                    .otp-code {
                        display: block;
                        margin: 20px auto;
                        text-align: center;
                        font-size: 32px;
                        font-weight: bold;
                        color: #007BFF;
                        background-color: #f9f9f9;
                        padding: 10px;
                        border: 1px dashed #007BFF;
                        width: fit-content;
                        border-radius: 8px;
                    }
                    .footer {
                        background-color: #f4f4f4;
                        text-align: center;
                        padding: 10px;
                        font-size: 12px;
                        color: #888888;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        Forgot Password Request
                    </div>
                    <div class="content">
                        <p>Hi %s,</p>
                        <p>We received a request to reset your password. Please use the OTP code below to proceed:</p>
                        <div class="otp-code">%s</div>
                        <p>If you did not request a password reset, please ignore this email or contact our support team for assistance.</p>
                        <p>For your security, this OTP will expire in 10 minutes.</p>
                        <p>Thank you for using our service!</p>
                    </div>
                    <div class="footer">
                        &copy; 2024 The Plutus Home. All rights reserved.<br>
                        This is an automated email; please do not reply.
                    </div>
                </div>
            </body>
            </html>
            """, username, otpCode);
    }
}
