package ch.makery.address;

import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by truposchet on 07.06.16.
 *
 */
public class CreateCareer {

    File folder;
    String filenames[];
    private SaveCarrier[] resultList;
    public CreateCareer(){
        folder = new File("Saves/");
        filenames = folder.list();
        resultList  = new SaveCarrier[filenames.length];
        for(int i = 0; i<filenames.length; i++){
            resultList[i]  =new SaveCarrier();
            resultList[i].filename=filenames[i];
            int j=0;
            int k = 0;
            double r = 0;
            try {DataInputStream dos = new DataInputStream(new FileInputStream("Saves/"+filenames[i]));
                r = dos.readDouble();
                r =dos.readDouble();
                r = dos.readDouble();
                r = dos.readDouble();
                j = dos.readInt();
                k = dos.readInt();
                dos.close();
            }catch (IOException ex){}
            j = j-k;
            resultList[i].coefficient = j;
        }
    }
    public String[] filesSort() {
        CreateCareer a =new  CreateCareer();
        String[] buffer = new String[resultList.length];
        long javaTime = 0, scalaTime = 0;
        ScalaSort scalaSort = new ScalaSort();


        javaTime = System.nanoTime();
        JavaQuickSort.quickSort(resultList);
        javaTime = System.nanoTime() - javaTime;

        scalaTime = System.nanoTime();
        scalaSort.sort(resultList);
        scalaTime = System.nanoTime() - scalaTime;

        double i = scalaSort.getSum(resultList);

        Alert infoMessage = new Alert(Alert.AlertType.INFORMATION);
        infoMessage.setTitle("Done");
        infoMessage.setHeaderText("Sort is done");
        infoMessage.setContentText(filenames.length + "  Files sorted\n" +
                "Java time is " + javaTime / 1000000 + "ms\n" +
                "Scala time is " + scalaTime / 1000000 + "ms\n"
        + "Average value is" + i + "\n"
        + resultList[0].filename);
        infoMessage.show();
        return buffer;
    }

}
