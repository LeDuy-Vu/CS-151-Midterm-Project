import java.util.Comparator ;

/**
 * A mailbox with its own extension number, password, list of maximum 3 greetings, an old and a new message list.
 */
public class Mailbox
{
	private final String extension ;
	private String password ;
	private String[] greetings ;
	private int greetingsCount ;
	private int currentGreeting ;
	private final MessageQueue newMessages ;
	private final MessageQueue oldMessages ;
	
	public static final int MAXIMUM_GREETING = 3 ;
	public static final int MAXIMUM_MESSAGE = 3 ;
	
	/**
	 * Constructs a mailbox object.
	 * @param extension the extension of the mailbox
	 * @param password the password of the mailbox
	 * @param greeting the default greeting of the mailbox
	 */
	public Mailbox(String extension, String password, String greeting)
	{
		this.extension = extension ;
		this.password = password ;
		greetings = new String[MAXIMUM_GREETING] ;
		greetings[0] = greeting ;
		currentGreeting = 0 ;
		greetingsCount = 1 ;
		newMessages = new MessageQueue(MAXIMUM_MESSAGE) ;
		oldMessages = new MessageQueue() ;
	}
	
	/**
	 * Creates a Comparator object that compares 2 mailboxes by extension number.
	 * @return a Comparator object
	 */
	public static Comparator<Mailbox> comparatorByExtension()
	{
		return new 
			Comparator<Mailbox>()
			{
				public int compare(Mailbox first, Mailbox second)
				{
					return first.getExtension().compareTo(second.getExtension());
				}
			};
	}
	
	/**
	 * Gets the extension number of the mailbox.
	 */
	public String getExtension()
	{
		return extension;
	}
	
	/**
	 * Checks if the password is correct.
	 * @param password the input password
	 * @return true if the passwords match
	 */
	public boolean checkPassword(String password)
	{
		return this.password.equals(password);
	}
	
	/**
	 * Sets the password to a new value.
	 * @param password the new password
	 * @precondition password.length() > 0
	 */
	public void setPassword(String password)
	{
		assert(password.length() > 0) : "Password can't be empty!" ;
		this.password = password ;
	}
	
	/**
	 * Gets the greeting currently being used.
	 * @return the current greeting
	 */
	public String getCurrentGreeting()
	{
		return greetings[currentGreeting];
	}
	
	/**
	 * Gets the position number of the currently used greeting.
	 * @return the greeting number
	 */
	public int getCurrentGreetingNumber()
	{
		return (currentGreeting + 1);
	}
	
	/**
	 * Gets the total number of greetings this mailbox has.
	 * @return the total number of greetings
	 */
	public int getGreetingsCount()
	{
		return greetingsCount;
	}
	
	/**
	 * Gets a specific greeting.
	 * @param i the position number of the desired greeting
	 * @return the desired greeting
	 * @precondition 1 <= i && i <= MAXIMUM_GREETING
	 */
	public String getSpecificGreeting(int i)
	{
		assert(1 <= i && i <= MAXIMUM_GREETING) : "Greeting number invalid!" ;
		return greetings[i - 1];
	}
	
	/**
	 * Switch the currently used greeting.
	 * @param i the number position of the new greeting
	 * @precondition 1 <= i && i <= MAXIMUM_GREETING
	 */
	public void switchGreeting(int i)
	{
		assert(1 <= i && i <= MAXIMUM_GREETING) : "Greeting number invalid!" ;
		currentGreeting = i - 1 ;
	}
	
	/**
	 * Adds a new greeting to the mailbox.
	 * @param greeting the content of the greeting to be added
	 * @precondition greeting.length() > 0
	 */
	public void recordGreeting(String greeting)
	{
		assert(greeting.length() > 0) : "Greeting can't be empty!" ;
		for (int i = 0; i < MAXIMUM_GREETING; i++)
			if (greetings[i] == null)
			{
				greetings[i] = greeting ;
				greetingsCount++ ;
				return;
			}
	}
	
	/**
	 * Deletes a greeting that is not the one currently used.
	 * @param i the number position of the new greeting
	 * @precondition 1 <= i && i <= MAXIMUM_GREETING && i != currentGreeting + 1
	 */
	public void deleteGreeting(int i)
	{
		assert(1 <= i && i <= MAXIMUM_GREETING && i != currentGreeting + 1) : "Greeting number invalid!" ;
		greetings[i - 1] = null ;
		greetingsCount-- ;
	}
	
	/**
	 * Gets the size of the new message queue.
	 * @return the new message queue size
	 */
	public int newSize()
	{
		return newMessages.size();
	}
	
	/**
	 * Gets the size of the old message queue.
	 * @return the old message queue size
	 */
	public int oldSize()
	{
		return oldMessages.size();
	}
	
	/**
	 * Gets the message at the front of the new queue.
	 * @return the first message in the new queue
	 * @precondition newSize() > 0
	 */
	public String newFront()
	{
		assert(newSize() > 0) ;
		return newMessages.front().getContent();
	}
	
	/**
	 * Gets the message at the front of the old queue.
	 * @return the first message in the old queue
	 * @precondition oldSize() > 0
	 */
	public String oldFront()
	{
		assert(oldSize() > 0) ;
		return oldMessages.front().getContent();
	}
	
	/**
	 * Adds a new message to the new message queue.
	 * @param content the content of the new message
	 * @precondition content.length() > 0
	 * @precondition !newMessages.isFull()
	 */
	public void recordNewMessage(String content)
	{
		assert(content.length() > 0) ;
		assert(!newMessages.isFull()) ;
		newMessages.add(new Message(content)) ;
	}
	
	/**
	 * Moves the front message of the new queue to the end of the old queue.
	 * @precondition newMessages.size() > 0
	 * @precondition !oldMessages.isFull()
	 */
	public void saveNewMessage()
	{
		assert(newMessages.size() > 0) ;
		assert(!oldMessages.isFull()) ;
		oldMessages.add(newMessages.remove());
	}
	
	/**
	 * Deletes the front message of the new queue.
	 * @precondition newSize() > 0
	 */
	public void removeNewMessage()
	{
		assert(newSize() > 0) ;
		newMessages.remove() ;
	}
	
	/**
	 * Deletes the front message of the old queue.
	 * @precondition oldSize() > 0
	 */
	public void removeOldMessage()
	{
		assert(oldSize() > 0) ;
		oldMessages.remove() ;
	}
	
	/**
	 * Gets the content of the current message of the old queue.
	 * @return the message content
	 * @precondition oldSize() > 0
	 */
	public String currentMessage()
	{
		assert(oldSize() > 0) ;
		return oldMessages.current().getContent();
	}
	
	/**
	 * Resets the current message pointer of the old queue back to the default position at head.
	 */
	public void resetCurrentMessage()
	{
		oldMessages.resetCurrent() ;
	}
	
	/**
	 * Moves the current message pointer to the next message in the old queue.
	 */
	public void advanceCurrentMessage()
	{
		oldMessages.advanceCurrent() ;
	}
}
