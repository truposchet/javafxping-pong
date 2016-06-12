package ch.makery.address;

import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by truposchet on 11.06.16.
 */
public class GoalsCreate {
    File folder;
    String filenames[];
    private Goals[] resultList;
    public GoalsCreate(){
        folder = new File("Saves/");
        filenames = folder.list();
        resultList  = new Goals[filenames.length];
        for(int i = 0; i<filenames.length; i++){
            resultList[i]  =new Goals();

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
            resultList[i].left = j;
            resultList[i].right = k;
        }
    }
    public String[] goalsStat(){
        GoalsCreate a =new GoalsCreate();
        String[] buffer = new String[resultList.length];
        ScalaStat scalaStat = new ScalaStat();

        double left =scalaStat.getSuLeft(resultList);
        double right = scalaStat.getSuRight(resultList);

        Alert infoMessage = new Alert(Alert.AlertType.INFORMATION);
        infoMessage.setTitle("Done");
        infoMessage.setHeaderText("Stat is done");
        infoMessage.setContentText("Left paddle goals avg " + left + "\n"
        +"Right paddle goals avg" + right + "\n");
        infoMessage.show();
        return buffer;
    }
}
