import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.drda.NetworkServerControl;

public class DerbyDB {
//	 static String driver = "org.apache.derby.jdbc.EmbeddedDriver";//��derby.jar����
//	 static String dbName ="UserDB";
//	 static String dbURL = "jdbc:derby:"+dbName+";create=true";//create=true��ʾ�����ݿⲻ����ʱ�ʹ�����
//	 static Connection conn;//����Ƕ��ʽ���ݿ�
//	 static Statement statement;
//     
	static Connection conn;
	static Statement statement;
	static NetworkServerControl derbyServer=null;

	static String driver = "org.apache.derby.jdbc.ClientDriver";//��derby.jar����
	static String dbName =".//UserDB";
	static String dbURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true"; 
	static PrintWriter pw;
	
	static public boolean prepareDB() {
  		try {
 			/*
 			 * * Load the Derby driver.* When the embedded Driver is used this
 			 * action start the Derby engine.* Catch an error and suggest a
 			 * CLASSPATH problem
 			 */
// 			Class.forName(driver);
// 			System.out.println(driver + " �ɹ����� ");
//			conn = DriverManager.getConnection(dbURL);
//			statement = conn.createStatement();

  		   derbyServer = new NetworkServerControl();
  		   //NetworkServerControl����derbynet.jar���� 
           pw = new PrintWriter(System.out);//��ϵͳ�����ΪDerby���ݿ����� 
           derbyServer.start(pw);//����Derby������ 
           Class.forName(driver); 
           System.out.println(driver + " �ɹ����� ");
           conn = DriverManager.getConnection(dbURL); 
           statement = conn.createStatement();
  		} catch (java.lang.ClassNotFoundException e) {
 			System.err.print("ClassNotFoundException: ");
 			System.err.println(e.getMessage());
 			System.out.println("\n    >>> ��˶����CLASSPATH variable   <<<\n");
 			return false;
 		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
  		return true;
	}
   
	static public void createAccountTable() {
    	 try {
//    		 statement.execute("drop table AccountTable");//����ɾ����
    		 if(!checkTable("AccountTable"))
    		 {
    			 statement.execute("create table AccountTable ("
    					 + "ID VARCHAR(10) primary key,"
    					 + "Password VARCHAR(20) NOT NULL,"
    					 + "Name VARCHAR(20) NOT NULL,"
    					 + "Status INT DEFAULT 0)");
    			 statement.executeUpdate("insert into AccountTable(ID,Password,Name,Status) "
    		   	       		+"values ('295579587','luojingyi','100',1)");
    			 //�������ӵ�һ����ϵͳ�õ�	��ǰID				��ǰ����			��һ��ע��ID	�Ƿ��ס���룬�Զ���¼0.1.2
    		 }
    	 } 
    	 catch(Exception e){e.printStackTrace();}
	}
   
	static public boolean insertAccountTable(String id,String password,String name,int status) {
    	 try {
      	   statement.executeUpdate("insert into AccountTable(ID,Password,Name,Status) "
   	       		+"values ('"+id+"','"+password+"','"+name+"',"+status+")");//����һ������
         } 
         catch(Exception e){e.printStackTrace();}

		return true;
	}
   
	static public boolean deleteAccountTable(String id)
    {
    	try {
			statement.executeUpdate("delete from AccountTable where ID='"+id+"'");
			return true;
		} 
    	catch (SQLException e) {e.printStackTrace();}
    	return false;
    }

	static public boolean resetStatus(String ID,int status)
	{
		try {
			statement.executeUpdate("update AccountTable set Status="+status+" where ID='"+ID+"'");
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	static public int getAccount0(String[] result)
     {
    	 try {
        	 ResultSet rs = statement.executeQuery("select * from AccountTable");//��ȡ�ղ��������
        	 rs.next();
    		 result[0]=rs.getString(1);
    		 result[1]=rs.getString(2);
    		 result[2]=rs.getString(3); 
    		 return rs.getInt(4);
         } 
         catch(Exception e)
    	 {
        	 e.printStackTrace();
        	 return -1;
         }
     }
  
	static public ResultSet getAccountTable()
    {
    	ResultSet rs = null;
		try {
			rs = statement.executeQuery("select * from AccountTable");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//��ȡ�ղ��������
    	return rs;
    }
  
	static public String getNewID()
    {
    	ResultSet rs = null;
		try {
			rs = statement.executeQuery("select * from AccountTable");
			rs.next();
			String id=rs.getString(3);
			String newid=""+(Integer.parseInt(id)+1);
			System.out.println(id);
			System.out.println(newid);
			statement.executeUpdate("update AccountTable set Name='"+newid+"' where Name='"+id+"'");
	    	return id;
		} 
		catch (SQLException e) {e.printStackTrace();}
		return null;
    }
   
	static public String getPassword(String id) {
		try {
			ResultSet resultSet=statement.executeQuery("select * from AccountTable where ID='"+id+"'");
			if(resultSet.next()){return resultSet.getString(2);}
		} 
		catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
	static public String getName(String id)
	{
		try {
			ResultSet resultSet=statement.executeQuery("select * from AccountTable where ID='"+id+"'");
			if(resultSet.next()){return resultSet.getString("Name");}
		} 
		catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
 	static public void showAccountTable()
    {
    	 try {
			ResultSet rs=getAccountTable();
			while(rs.next()){
				 System.out.println("IDΪ��"+rs.getString(1)
				 +"\t����Ϊ��"+rs.getString(2)
				 +"\t�ǳ�Ϊ��"+rs.getString(3)
				 +"\t״̬Ϊ��"+rs.getString(4));
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  
    static public boolean checkTable(String tableName) throws SQLException {
		try {
			statement.execute("update "+tableName
					+" set ID= 'TEST', Name = 'testing' where 1=100");
		} 
		catch (SQLException sqle) {
			String theError = (sqle).getSQLState();
			/** If table exists will get - WARNING 02000: No row was found **/
			if (theError.equals("42X05")) //������
			{return false;} 
			else if (theError.equals("42X14") || theError.equals("42821")) {
				System.out.println("checkTable: ����ı���. ɾ����USERTABLE �� ������ program");
				throw sqle;
			} else {
				System.out.println("checkTable: Unhandled SQLException");
				throw sqle;
			}
		}
		return true;
	}
  	
    static public void closeDerbyDB()
  	{

      try {derbyServer.shutdown();} //�ر�Derby������ 
      catch (Exception e) {e.printStackTrace();}
  	}
}