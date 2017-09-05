package BikeShop.control;

import java.io.IOException;
public class DatabaseBackup {
   public static void backup(){
       try {
           String filename = null;
           String path = "D:\\xampp\\mysql\\bin\\mysqldump";
           String command = "cmd.exe /c "
                   + "\"\"" + path + "\"  "
                   + " --user=admin"
                   + " --password=12345"
                   + " --host=localhost"
                   + " --protocol=tcp "
                   + " --port=3306"
                   + " --default-character-set=utf8 "
                   + " --single-transaction=TRUE "
                   + " --routines "
                   + " --events "
                   + "\"bikedb\""
                   + ">"
                   + " \""
                   + "D:\\DB_Backup.sql"
                   + "\"";

           Process child = Runtime.getRuntime().exec(command);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}