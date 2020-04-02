package MD5;
import java.security.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class MD5 {

    private static final boolean RECURSIVE = false; // uwzglednienie podkatalogow

    public static void main(String[] args) {
	// write your code here

        // sciezka do katalogu
        String dirPathname = "Testowy";

        // sciezka do pliku
        String filepath = "Pojedynczy.txt";
        File directory = new File(dirPathname);

        if(!directory.isDirectory()){
            System.out.println(dirPathname + " is not directory");
        }

        try {
            //saveFilesChecksumes(directory);
            checkFilesChecksumes(directory);
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            //saveFileChecksume(filepath);
            verifyFileChecksume(filepath);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Bad path, or file was deleted");
        }
    }

    public static void saveFilesChecksumes(File directory) throws IOException {

        File[] files = directory.listFiles();
        PrintWriter saveFile = new PrintWriter("checksumes.txt");


        for (File file : files) {

            if(file.isFile()){

                try {

                    String filename = file.getAbsolutePath();
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(Files.readAllBytes(Paths.get(filename)));
                    byte[] digest = md.digest();
                    String myChecksum = Base64.getEncoder().encodeToString(digest);

                    saveFile.println(myChecksum);

                } catch (NoSuchAlgorithmException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("One or more files were deleted");
                }

            } else if(file.isDirectory() && RECURSIVE){

                saveFilesChecksumes(file);

            }

        }
        saveFile.close();
    }

    public static void checkFilesChecksumes(File directory) throws IOException {

        File[] files = directory.listFiles();
        Scanner scanner = new Scanner(new File("checksumes.txt"));
        List<String> checkSumesList = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            checkSumesList.add(scanner.nextLine());
        }

        boolean modified = false;
        int count = 0;

        for (File file : files) {

            if(file.isFile()){

                try {

                    String filename = file.getAbsolutePath();
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(Files.readAllBytes(Paths.get(filename)));
                    byte[] digest = md.digest();
                    String myChecksum = Base64.getEncoder().encodeToString(digest);

                    //String checksum = scanner.nextLine();
                    System.out.println(filename + "  :  " + myChecksum);
                    System.out.println(checkSumesList.contains(myChecksum) ? "MATCH, file was not modified" : "NO MATCH, file was modified");
                    if(!checkSumesList.contains(myChecksum))
                        modified = true;
                    count++;

                } catch (NoSuchAlgorithmException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("One or more files were deleted or added");
                    modified = true;
                }

            } else if(file.isDirectory() && RECURSIVE){

                checkFilesChecksumes(file);

            }

        }

        if(modified || count != checkSumesList.size())
            System.out.println("Files were modified or deleted!!!");
        else
            System.out.println("Files were not modified");
    }

    public static void saveFileChecksume(String filepath) throws NoSuchAlgorithmException, IOException{

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(filepath)));
        byte[] digest = md.digest();
        String myChecksum = Base64.getEncoder().encodeToString(digest);

        PrintWriter saveFile = new PrintWriter("checksume.txt");
        saveFile.println(myChecksum);
        saveFile.close();
    }

    public static void verifyFileChecksume(String filepath) throws NoSuchAlgorithmException, IOException{

        Scanner scanner = new Scanner(new File("checksume.txt"));
        String checksum = scanner.nextLine();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(filepath)));
        byte[] digest = md.digest();
        String myChecksum = Base64.getEncoder().encodeToString(digest);


        System.out.println(filepath + "  :  " + myChecksum);
        System.out.println(checksum.equalsIgnoreCase(myChecksum) ? "MATCH, file was not modified" : "NO MATCH, file was modified");
    }


    public static void givenFile_generatingChecksum_thenVerifying() throws NoSuchAlgorithmException, IOException {

        String filename = "Pojedynczy.txt";
        String checksum = "4vskniRngFjwPLIU3qQNcg==";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(filename)));
        byte[] digest = md.digest();
        String myChecksum = Base64.getEncoder().encodeToString(digest);

        System.out.println(myChecksum);
        System.out.println(checksum.equalsIgnoreCase(myChecksum) ? "MATCH" : "NO MATCH");
    }

}
