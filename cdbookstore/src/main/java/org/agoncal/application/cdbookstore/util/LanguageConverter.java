package org.agoncal.application.cdbookstore.util;

import static org.agoncal.application.cdbookstore.util.Language.DEUTSCH;
import static org.agoncal.application.cdbookstore.util.Language.ENGLISH;
import static org.agoncal.application.cdbookstore.util.Language.FINISH;
import static org.agoncal.application.cdbookstore.util.Language.FRENCH;
import static org.agoncal.application.cdbookstore.util.Language.GERMAN;
import static org.agoncal.application.cdbookstore.util.Language.ITALIAN;
import static org.agoncal.application.cdbookstore.util.Language.PORTUGUESE;
import static org.agoncal.application.cdbookstore.util.Language.RUSSIAN;
import static org.agoncal.application.cdbookstore.util.Language.SPANISH;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(final Language language) {
        switch (language) {
        case DEUTSCH:
            return "DE";
        case ENGLISH:
            return "EN";
        case FINISH:
            return "FI";
        case FRENCH:
            return "FR";
        case GERMAN:
            return "GM";
        case ITALIAN:
            return "IT";
        case PORTUGUESE:
            return "PT";
        case RUSSIAN:
            return "RU";
        case SPANISH:
            return "SP";
        default:
            throw new IllegalArgumentException("Unknown" + language);
        }
    }

    @Override
    public Language convertToEntityAttribute(final String dbData) {
        switch (dbData) {
        case "DE":
            return DEUTSCH;
        case "EN":
            return ENGLISH;
        case "FI":
            return FINISH;
        case "FR":
            return FRENCH;
        case "GM":
            return GERMAN;
        case "IT":
            return ITALIAN;
        case "PT":
            return PORTUGUESE;
        case "RU":
            return RUSSIAN;
        case "SP":
            return SPANISH;
        default:
            throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}
