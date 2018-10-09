import java.sql.*;

public class JdbcUtils{
    private JdbcUtils(){}  //类私有化

    private static String url = "jdbc:mysql://localhost/db_name";
    private static String user = "root";
    private static String password = "";

    static{
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(Exception e){System.out.println(e.toString());}
    }

    public static Connection getConnection()throws Exception{
        return DriverManager.getConnection(url,user,password);
    }

    public static void freeInstance(ResultSet rs,Statement sttm,Connection cnnt){
        try{
            if(rs!=null)rs.close();
        }catch(Exception e){System.out.println(e.toString());}
        finally{
            try{
                if(sttm!=null)sttm.close();
            }catch(Exception e){System.out.println(e.toString());}
            finally{
                try{
                    if(cnnt!=null)cnnt.close();
                }catch(Exception e){System.out.println(e.toString());}
            }
        }
    }
}
