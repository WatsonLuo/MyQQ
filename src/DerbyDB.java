import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.drda.NetworkServerControl;

public class DerbyDB {
//	 static String driver = "org.apache.derby.jdbc.EmbeddedDriver";//在derby.jar里面
//	 static String dbName ="UserDB";
//	 static String dbURL = "jdbc:derby:"+dbName+";create=true";//create=true表示当数据库不存在时就创建它
//	 static Connection conn;//启动嵌入式数据库
//	 static Statement statement;
//     
	static Connection conn;
	static Statement statement;
	static NetworkServerControl derbyServer=null;

	static String driver = "org.apache.derby.jdbc.ClientDriver";//在derby.jar里面
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
// 			System.out.println(driver + " 成功加载 ");
//			conn = DriverManager.getConnection(dbURL);
//			statement = conn.createStatement();

  		   derbyServer = new NetworkServerControl();
  		   //NetworkServerControl类在derbynet.jar里面 
           pw = new PrintWriter(System.out);//用系统输出作为Derby数据库的输出 
           derbyServer.start(pw);//启动Derby服务器 
           Class.forName(driver); 
           System.out.println(driver + " 成功加载 ");
           conn = DriverManager.getConnection(dbURL); 
           statement = conn.createStatement();
  		} catch (java.lang.ClassNotFoundException e) {
 			System.err.print("ClassNotFoundException: ");
 			System.err.println(e.getMessage());
 			System.out.println("\n    >>> 请核对你的CLASSPATH variable   <<<\n");
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
//    		 statement.execute("drop table AccountTable");//用于删除表
    		 if(!checkTable("AccountTable"))
    		 {
    			 statement.execute("create table AccountTable ("
    					 + "ID VARCHAR(10) primary key,"
    					 + "Password VARCHAR(20) NOT NULL,"
    					 + "Name VARCHAR(20) NOT NULL,"
    					 + "Status INT DEFAULT 0)");
    			 statement.executeUpdate("insert into AccountTable(ID,Password,Name,Status) "
    		   	       		+"values ('295579587','luojingyi','100',1)");
    			 //这里增加的一条是系统用的	当前ID				当前密码			下一个注册ID	是否记住密码，自动登录0.1.2
    		 }
    	 } 
    	 catch(Exception e){e.printStackTrace();}
	}
   
	static public boolean insertAccountTable(String id,String password,String name,int status) {
    	 try {
      	   statement.executeUpdate("insert into AccountTable(ID,Password,Name,Status) "
   	       		+"values ('"+id+"','"+password+"','"+name+"',"+status+")");//插入一条数据
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
        	 ResultSet rs = statement.executeQuery("select * from AccountTable");//读取刚插入的数据
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
		}//读取刚插入的数据
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
				 System.out.println("ID为："+rs.getString(1)
				 +"\t密码为："+rs.getString(2)
				 +"\t昵称为："+rs.getString(3)
				 +"\t状态为："+rs.getString(4));
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
			if (theError.equals("42X05")) //表不存在
			{return false;} 
			else if (theError.equals("42X14") || theError.equals("42821")) {
				System.out.println("checkTable: 错误的表定义. 删除表USERTABLE 并 重做该 program");
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

      try {derbyServer.shutdown();} //关闭Derby服务器 
      catch (Exception e) {e.printStackTrace();}
  	}
}