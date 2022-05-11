package pl.agnieszkajankowska.enauczyciel.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageService {

    private String fileName;

    public String getPathToAttachedImage() {
        return "src/main/resources/static/uploads/";
    }

    public String saveAttachedImage(MultipartFile file, String pathToUploadDirectory) throws IOException {
        File uploadDirectory = new File(pathToUploadDirectory);
        uploadDirectory.mkdirs();

        List<Path> listOfFilesNameInDB = getListOfFileInDB(pathToUploadDirectory);

        setFileName(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase());
        changeNameOfFileIfAlreadyExistInDB(listOfFilesNameInDB, fileName);

        File loadedFile = new File(pathToUploadDirectory + fileName);

        OutputStream outputStream = new FileOutputStream(loadedFile);
        InputStream inputStream = file.getInputStream();

        IOUtils.copy(inputStream, outputStream);

        return "/uploads/" + fileName;
    }

    //PRIVATE METHODS
    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private  List<Path> getListOfFileInDB(String pathToUploadDirectory) throws IOException {

        Stream<Path> walk = Files.walk(Path.of(pathToUploadDirectory));
        return walk.filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    private String addNumberToRedundantNameOfFile(String fileNameInLowerCases) {

        int indexOdComa = fileNameInLowerCases.indexOf('.');
        return fileNameInLowerCases.substring(0, indexOdComa) + "(copy)" + fileNameInLowerCases.substring(indexOdComa);
    }

    private void changeNameOfFileIfAlreadyExistInDB(List<Path> listOfFilesNameInDB, String fileName) {
        for (Path result: listOfFilesNameInDB) {
            if(result.getFileName().toString().equals(fileName)) {
                String fileNameChanged = addNumberToRedundantNameOfFile(fileName);
                setFileName(fileNameChanged);
                changeNameOfFileIfAlreadyExistInDB(listOfFilesNameInDB, fileNameChanged);
            }
        }
    }
}
