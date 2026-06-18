package pl.edu.ecommerceshop.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCodeEmailService {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Warsaw");

    private static final DateTimeFormatter EXPIRATION_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(DEFAULT_ZONE_ID);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${shopflow.security.login-code.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${shopflow.security.login-code.email.from:no-reply@shopflow.local}")
    private String from;

    public void sendLoginCode(String to, String code, Instant expiresAt) {
        if (!emailEnabled) {
            log.warn("Login verification code for {} is {} and expires at {}", to, code, expiresAt);
            return;
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();

        if (mailSender == null) {
            log.warn("JavaMailSender is not configured. Login verification code for {} is {} and expires at {}", to, code, expiresAt);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Twój kod logowania do ShopFlow");
            helper.setText(createPlainTextMessage(code, expiresAt), createHtmlMessage(code, expiresAt));

            mailSender.send(message);
        } catch (MessagingException | MailException exception) {
            log.error("Could not send login verification code to {}", to, exception);
            throw new IllegalStateException("Could not send login verification code.", exception);
        }
    }

    private String createPlainTextMessage(String code, Instant expiresAt) {
        return """
                Twój kod logowania do ShopFlow:

                %s

                Kod jest ważny do: %s

                Jeśli to nie Ty próbujesz się zalogować, zignoruj tę wiadomość.
                """.formatted(code, formatExpirationTime(expiresAt));
    }

    private String createHtmlMessage(String code, Instant expiresAt) {
        String expirationTime = formatExpirationTime(expiresAt);

        return """
                <!DOCTYPE html>
                <html lang="pl">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Kod logowania ShopFlow</title>
                </head>
                <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                    <table width="100%%" cellpadding="0" cellspacing="0" role="presentation" style="background-color:#f4f6f8; padding:32px 16px;">
                        <tr>
                            <td align="center">
                                <table width="100%%" cellpadding="0" cellspacing="0" role="presentation" style="max-width:560px; background-color:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 8px 24px rgba(15, 23, 42, 0.08);">
                                    <tr>
                                        <td style="background:linear-gradient(135deg, #111827, #2563eb); padding:28px 32px; text-align:center;">
                                            <div style="font-size:26px; font-weight:700; color:#ffffff; letter-spacing:0.3px;">
                                                ShopFlow
                                            </div>
                                            <div style="font-size:14px; color:#dbeafe; margin-top:6px;">
                                                Bezpieczne logowanie
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding:32px;">
                                            <h1 style="margin:0 0 12px; font-size:22px; line-height:1.35; color:#111827;">
                                                Twój kod logowania
                                            </h1>

                                            <p style="margin:0 0 24px; font-size:15px; line-height:1.6; color:#4b5563;">
                                                Użyj poniższego kodu, aby dokończyć logowanie do aplikacji ShopFlow.
                                            </p>

                                            <div style="background-color:#f9fafb; border:1px solid #e5e7eb; border-radius:14px; padding:24px; text-align:center; margin-bottom:24px;">
                                                <div style="font-size:13px; color:#6b7280; text-transform:uppercase; letter-spacing:1.2px; margin-bottom:10px;">
                                                    Kod weryfikacyjny
                                                </div>

                                                <div style="font-size:38px; line-height:1; font-weight:800; letter-spacing:8px; color:#111827;">
                                                    %s
                                                </div>
                                            </div>

                                            <p style="margin:0 0 16px; font-size:14px; line-height:1.6; color:#4b5563;">
                                                Kod jest ważny do:
                                                <strong style="color:#111827;">%s</strong>
                                            </p>

                                            <div style="background-color:#fff7ed; border:1px solid #fed7aa; border-radius:12px; padding:14px 16px; margin-top:22px;">
                                                <p style="margin:0; font-size:13px; line-height:1.5; color:#9a3412;">
                                                    Jeśli to nie Ty próbujesz się zalogować, zignoruj tę wiadomość.
                                                    Nie przekazuj tego kodu żadnej innej osobie.
                                                </p>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="background-color:#f9fafb; padding:20px 32px; text-align:center; border-top:1px solid #e5e7eb;">
                                            <p style="margin:0; font-size:12px; line-height:1.5; color:#6b7280;">
                                                Ta wiadomość została wysłana automatycznie. Prosimy na nią nie odpowiadać.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(code, expirationTime);
    }

    private String formatExpirationTime(Instant expiresAt) {
        return EXPIRATION_FORMATTER.format(expiresAt);
    }
}