package com.iiui.studentapp.backend.service;

import com.iiui.studentapp.backend.model.StudentInfo;

import java.util.Locale;

public class StudentEmailParser {
    public StudentInfo parse(String rawEmail, String rawBatch) throws ValidationException {
        if (rawEmail == null || rawEmail.trim().isEmpty()) {
            throw new ValidationException("Email field empty nahi ho sakti.");
        }

        String email = rawEmail.trim().toLowerCase(Locale.ROOT);
        validateEmail(email);

        if (rawBatch == null || rawBatch.trim().isEmpty()) {
            throw new ValidationException("Batch field empty nahi ho sakti. Example: F22");
        }

        String batch = rawBatch.trim().toUpperCase(Locale.ROOT);
        String localPart = email.substring(0, email.indexOf('@'));
        String name = localPart.substring(0, localPart.indexOf('.'));
        String degreeAndReg = localPart.substring(localPart.indexOf('.') + 1);
        StringBuilder degreeTitle = new StringBuilder();
        StringBuilder regNo = new StringBuilder();

        if (!name.matches("[a-zA-Z]+")) {
            throw new ValidationException("Invalid name. Name part must contain alphabets only.");
        }

        for (char character : degreeAndReg.toCharArray()) {
            if (Character.isLetter(character)) {
                degreeTitle.append(character);
            } else if (Character.isDigit(character)) {
                regNo.append(character);
            } else {
                throw new ValidationException("Degree aur Reg# part me sirf alphabets aur digits allowed hain.");
            }
        }

        if (degreeTitle.length() < 4 || !degreeTitle.toString().matches("[a-zA-Z]+")) {
            throw new ValidationException("Invalid degree title. Minimum 4 alphabetic characters required.");
        }

        if (regNo.length() < 1 || regNo.length() > 4) {
            throw new ValidationException("Invalid Reg#. Must be 1 to 4 digits.");
        }

        return new StudentInfo(
                email,
                regNo.toString(),
                capitalize(name),
                degreeTitle.toString().toUpperCase(Locale.ROOT),
                batch
        );
    }

    private void validateEmail(String email) throws ValidationException {
        if (!email.contains("@")) {
            throw new ValidationException("Invalid email. @ missing hai.");
        }

        if (!email.endsWith("@iiu.edu.pk") && !email.endsWith("@student.iiu.edu.pk")) {
            throw new ValidationException("Invalid email. Domain must be @iiu.edu.pk or @student.iiu.edu.pk");
        }

        String localPart = email.substring(0, email.indexOf('@'));
        if (localPart.isEmpty() || !localPart.contains(".")) {
            throw new ValidationException("Email format error. Must be like: name.degreereg@iiu.edu.pk");
        }

        if (localPart.substring(0, localPart.indexOf('.')).isEmpty()
                || localPart.substring(localPart.indexOf('.') + 1).isEmpty()) {
            throw new ValidationException("Email format error. Name and degree/reg part required.");
        }
    }

    private String capitalize(String value) {
        if (value.isEmpty()) {
            return value;
        }

        return value.substring(0, 1).toUpperCase(Locale.ROOT)
                + value.substring(1).toLowerCase(Locale.ROOT);
    }
}
