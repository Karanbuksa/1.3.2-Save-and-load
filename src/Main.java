import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static List<String> paths = new ArrayList<>();

    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(16, 5, 10, 8000);
        GameProgress gameProgress2 = new GameProgress(16, 7, 99, 8000);
        GameProgress gameProgress3 = new GameProgress(16, 5, 99, 8000);
        saveGame("D:\\Games\\savegames\\gameprog1.dat", gameProgress1);
        saveGame("D:\\Games\\savegames\\gameprog2.dat", gameProgress2);
        saveGame("D:\\Games\\savegames\\gameprog3.dat", gameProgress3);
        zipFiles("D:\\Games\\savegames\\savedgames.zip", paths);
        for (String path : paths) {
            File file = new File(path);
            file.delete();
        }
        openZip("D:\\Games\\savegames\\savedgames.zip", "D:\\Games\\savegames\\");
        System.out.println(openProgress("D:\\Games\\savegames\\gameprog1.dat"));
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream saveGamePath = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(saveGamePath)) {
            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        paths.add(path);
    }

    public static void zipFiles(String archivePath, List<String> archivingObjectsPathList) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(archivePath))) {
            for (String next : archivingObjectsPathList) {
                File file = new File(next);
                try (FileInputStream fileInputStream = new FileInputStream(next)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String pathToArchive, String pathToDir) {
        try (FileInputStream fileInputStream = new FileInputStream(pathToArchive);
             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)
        ) {
            ZipEntry zipEntry;
            String name;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                name = zipEntry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(pathToDir + name);
                for (int i = zipInputStream.read(); i != -1; i = zipInputStream.read()) {
                    fileOutputStream.write(i);
                }
                fileOutputStream.flush();
                zipInputStream.closeEntry();
                fileOutputStream.close();
            }

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static GameProgress openProgress(String savePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fileInputStream = new FileInputStream(savePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            gameProgress = (GameProgress) objectInputStream.readObject();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return gameProgress;
    }
}