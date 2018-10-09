import java.sql.*;

public class Main
{
    public static void main(String args[])throws Exception
    {
        Connection cttn = null;
        Statement sttm = null;
        ResultSet rs = null;
        String query_instruct = "select * from table_name";
        try{
            cttn = JdbcUtils.getConnection();
            sttm = cttn.createStatement();
            rs = sttm.executeQuery(query_instruct);
            while(rs.next())
            {
                //略
            }
        }finally{JdbcUtils.freeInstance(rs,sttm,cttn);}

    }
}
