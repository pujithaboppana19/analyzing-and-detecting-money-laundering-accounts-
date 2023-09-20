public static int findSpamUserBasedOnLoginHistory(String userName)
	{
		int result=1;
		User user=DAO.getUserById(userName);
		
		Date currentDate=new Date();
	//Subtract current date from registration date
	    long difference = currentDate.getTime()-user.getRegDate().getTime();
      
	    long totalDays= difference / (24 * 60 * 60 * 1000);
long loggedInDays=DAO.getUserLoginDaysCount(userName);
	    long notloggedinDays=totalDays-loggedInDays;
        float res1=(float)loggedInDays/(float)totalDays;
	    float res2=res1*100;
System.out.println(userName+"total days of profile:\t"+totalDays);
       System.out.println(userName+" logged in days "+loggedInDays);
System.out.println(userName+"not logged in days :\t"+notloggedinDays);
		
		System.out.println(userName+" login per :\t"+res2);
              //If number of login days is less than constant then he is spam
		if(Constants.loginDaysPer>res2)
		{
			System.out.println("in if 1");
	result=0;
		}
		System.out.println("Result is :\t"+result);
		
		return result;
	}
public static int findSpamUserBasedOnFriends(String userName)
	{	
	int noOfFriends=DAO.getFriendsList(userName).size();
		
		System.out.println(noOfFriends);
		//if no.of friends is less than 2 then he is spam
		if(Constants.noOfFirends>noOfFriends)
		{
			return 0;
		}
		
		return 1;
	}
public static int findSpamUserBasedOnServices(String userName)
	{	
	int noOfServices=DAO.getUser_ServiceList(userName).size();
		
		System.out.println(noOfServices);
		//if no.of services used is less than 1 then he is spam
	if(Constants.noOfServices>DAO.getUser_ServiceList(userName).size())
		{
			return 0;
		}
		
		return 1;
	}
public static int findSpamUserBasedOnTransaction(String userName)
	{	
	int result=1;
Set<Transaction>transactions=DAO.getUserById(userName).getTransactions();
	System.out.println("transactions size is :\t"+transactions.size());
	int noOfReceivedTransactions=0;
		//Iterating through each transaction
		for(Transaction transaction : transactions)
		{
			if(transaction.getType().equals("received"))
{
				noOfReceivedTransactions++;
			}
		}
int noOfEvents=DAO.getUser_EventList(userName).size();
		
	System.out.println(noOfEvents+"\t"+noOfReceivedTransactions);
		//if no.of  events is greater than no.of transactions transactions then he is spam
		if(noOfEvents>noOfReceivedTransactions)
		{
			result=0;
		}
		
		return result;
	}
public static int findSpamUserBasedOnSpendAmmount(String userName)
	{	
		float transactionAmount=0;
		
  List<Transaction> transactions=DAO.getUserTransactionList(userName);
	
     System.out.println("transactions size is :\t"+transactions.size());
	//Iterating through all transactions
	for(Transaction transaction : transactions)
	{		
         if(transaction.getType().equals("received"))
	{
    transactionAmount=transactionAmount+transaction.getAmount();
	}
}
System.out.println("total transaction amount \t"+transactionAmount);
float user_EventAmount=0;
		
List<User_Event> user_Events=DAO.getUser_EventList(userName);
	//Iterating through each user event
	for(User_Event user_Event : user_Events)
	{
	System.out.println("user event each form "+user_Event);
			
	user_EventAmount=user_EventAmount+DAO.getEventById(user_Event.geteId()).getPrice();
	}	
System.out.println("user events
amount\t"+user_EventAmount);
float userGiftAmount=0;
	
List<Gift> gifts=DAO.getUserGiftedList(userName);
for(Gift gift : gifts)
{
		userGiftAmount=userGiftAmount+gift.getAmount();
}	
System.out.println("user gifting ammount "+userGiftAmount);
//if amount received through transactions is less than event amounts or user gift amount then he/she is spam
if(transactionAmount<user_EventAmount || transactionAmount<userGiftAmount)
{
      return 0;
}
return 1;
}
public static boolean isSpamUser(String userName)
{
      boolean flag=false;
       ArrayList<Integer> list=new ArrayList<Integer>();
	//Calling all five functions to evaluate if user is spam or not
      list.add(findSpamUserBasedOnLoginHistory(userName));
	 list.add(findSpamUserBasedOnFriends(userName));
     list.add(findSpamUserBasedOnServices(userName));
	list.add(findSpamUserBasedOnTransaction(userName));
   list.add(findSpamUserBasedOnSpendAmmount(userName));	
	System.out.println("spam results is :\t"+list);
     int count=0;
for(int i : list)
{
     if(i==0)
      {
           count++;
       }
}
//if more than 2 functions return spam then the user is spam
if(count>=3)
{
	flag=true;
}
		
return flag;
}
}
