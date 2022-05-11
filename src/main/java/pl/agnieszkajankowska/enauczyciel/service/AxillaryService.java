package pl.agnieszkajankowska.enauczyciel.service;

public class AxillaryService {

    public static String formatTextBeforeSaveInDB(String text) {
        return text.substring(0, 1).toUpperCase() + text.toLowerCase().substring(1);
    }
}
