import java.util.* ;

/**
 * A center holding a sorted list of mailboxes based on their extension number.
 */
public class MailCenter
{
	private final ArrayList<Mailbox> mailboxes ;
	
	/**
	 * Constructs a mail center with no mailbox inside.
	 */
	public MailCenter()
	{
		mailboxes = new ArrayList<Mailbox>() ;
	}
	
	/**
	 * Gets the total number of mailboxes in the list.
	 * @return the number of mailboxes
	 */
	public int size()
	{
		return mailboxes.size();
	}
	
	/**
	 * Adds a new mailbox to the list with its own extension, password, and default greeting,
	 * then sorts the list based on extension number
	 * @param extension
	 * @param password
	 */
	public void add(String extension, String password)
	{
		String greeting = "You have reached mailbox " + extension + ".\nPlease leave your message now.\n" ;
		mailboxes.add(new Mailbox(extension, password, greeting)) ;
		Collections.sort(mailboxes, Mailbox.comparatorByExtension()) ;
	}
	
	/**
	 * Checks if the mailbox with supplied extension exists in the list.
	 * @param extension the extension number needed to be checked
	 * @return true if such a mailbox exists
	 */
	public boolean checkMailbox(String extension)
	{
		for (Mailbox m: mailboxes)
			if (m.getExtension().equals(extension))
				return true;
		return false;
	}
	
	/**
	 * Gets the mailbox that has provided extension.
	 * @param extension the extension number of the desired mailbox
	 * @return the Mailbox object
	 * @precondition checkMailbox(extension)
	 */
	public Mailbox get(String extension)
	{
		assert(checkMailbox(extension)) : "Mailbox doesn't exist!" ;
		for (Mailbox m: mailboxes)
			if (m.getExtension().equals(extension))
				return m;
		return null;
	}
	
	/**
	 * Checks if the password of the mailbox with provided extension is correct.
	 * @param extension the extension number of the mailbox
	 * @param password the input password
	 * @return true if the passwords match
	 */
	public boolean checkPassword(String extension, String password)
	{
		return get(extension).checkPassword(password);
	}
	
	/**
	 * Sets the password of the mailbox with provided extension to a new value.
	 * @param extension the extension number of the mailbox
	 * @param password the input password
	 * @precondition password.length() > 0
	 */
	public void setPassword(String extension, String password)
	{
		assert(password.length() > 0) ;
		get(extension).setPassword(password) ;
	}
	
	/**
	 * Resets all mailboxes password back to theirs default passwords, which are the same as their extension number
	 * @precondition mailboxes.size() > 0
	 */
	public void resetAllPassword()
	{
		assert (mailboxes.size() > 0) : "There is no mailbox in the system!" ;
		for (Mailbox m: mailboxes)
			m.setPassword(m.getExtension()) ;
	}
	
	/**
	 * Gets the greeting currently being used of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the current greeting
	 */
	public String getCurrentGreeting(String extension)
	{
		return get(extension).getCurrentGreeting();
	}
	
	/**
	 * Gets the position number of the currently used greeting of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the greeting number
	 */
	public int getCurrentGreetingNumber(String extension)
	{
		return get(extension).getCurrentGreetingNumber();
	}
	
	/**
	 * Gets the total number of greetings the mailbox with provided extension has.
	 * @param extension the extension number of the mailbox
	 * @return the total number of greetings
	 */
	public int getGreetingsCount(String extension)
	{
		return get(extension).getGreetingsCount() ;
	}
	
	/**
	 * Gets a specific greeting of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @param greeting the position number of the desired greeting
	 * @return the desired greeting
	 * @precondition 1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING
	 */
	public String getSpecificGreeting(String extension, int greeting)
	{
		assert(1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING) ;
		return get(extension).getSpecificGreeting(greeting);
	}
	
	/**
	 * Switch the currently used greeting of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @param greeting the number position of the new greeting
	 * @precondition 1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING
	 */
	public void switchGreeting(String extension, int greeting)
	{
		assert(1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING) ;
		get(extension).switchGreeting(greeting) ;
	}
	
	/**
	 * Adds a new greeting to the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @param greeting the content of the greeting to be added
	 * @precondition greeting.length() > 0
	 */
	public void recordGreeting(String extension, String greeting)
	{
		assert(greeting.length() > 0) ;
		get(extension).recordGreeting(greeting) ;
	}
	
	/**
	 * Deletes a greeting that is not the one currently used in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @param greeting the number position of the new greeting
	 * @precondition 1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING && greeting != getCurrentGreetingNumber(extension)
	 */
	public void deleteGreeting(String extension, int greeting)
	{
		assert(1 <= greeting && greeting <= Mailbox.MAXIMUM_GREETING && greeting != getCurrentGreetingNumber(extension)) ;
		get(extension).deleteGreeting(greeting) ;
	}
	
	/**
	 * Gets the size of the new message queue of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the new message queue size
	 */
	public int newSize(String extension)
	{
		return get(extension).newSize();
	}
	
	/**
	 * Gets the size of the old message queue of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the old message queue size
	 */
	public int oldSize(String extension)
	{
		return get(extension).oldSize();
	}
	
	/**
	 * Gets the message at the front of the new queue of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the first message in the new queue
	 * @precondition newSize(extension) > 0
	 */
	public String newFront(String extension)
	{
		assert(newSize(extension) > 0) ;
		return get(extension).newFront();
	}
	
	/**
	 * Gets the message at the front of the old queue of the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the first message in the old queue
	 * @precondition oldSize(extension) > 0
	 */
	public String oldFront(String extension)
	{
		assert(oldSize(extension) > 0) ;
		return get(extension).oldFront() ;
	}
	
	/**
	 * Adds a new message to the new message queue in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @param content the content of the new message
	 * @precondition content.length() > 0
	 * @precondition newSize(extension) <= Mailbox.MAXIMUM_MESSAGE
	 */
	public void recordNewMessage(String extension, String content)
	{
		assert(content.length() > 0) ;
		assert(newSize(extension) <= Mailbox.MAXIMUM_MESSAGE) ;
		get(extension).recordNewMessage(content) ;
	}
	
	/**
	 * Moves the front message of the new queue to the end of the old queue in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @precondition newSize(extension) > 0
	 * @precondition oldSize(extension) <= Mailbox.MAXIMUM_MESSAGE
	 */
	public void saveNewMessage(String extension)
	{
		assert(newSize(extension) > 0) ;
		assert(oldSize(extension) <= Mailbox.MAXIMUM_MESSAGE) ;
		get(extension).saveNewMessage();
	}
	
	/**
	 * Deletes the front message of the new queue in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @precondition newSize(extension) > 0
	 */
	public void removeNewMessage(String extension)
	{
		assert(newSize(extension) > 0) ;
		get(extension).removeNewMessage() ;
	}
	
	/**
	 * Deletes the front message of the old queue in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @precondition oldSize(extension) > 0
	 */
	public void removeOldMessage(String extension)
	{
		assert(oldSize(extension) > 0) ;
		get(extension).removeOldMessage() ;
	}
	
	/**
	 * Gets the content of the current message of the old queue in the mailbox with provided extension.
	 * @param extension the extension number of the mailbox
	 * @return the message content
	 * @precondition oldSize(extension) > 0
	 */
	public String currentMessage(String extension)
	{
		assert(oldSize(extension) > 0) ;
		return get(extension).currentMessage();
	}
	
	/**
	 * Resets the current message pointer of the old queue back to the default position at head.
	 * @param extension the extension number of the mailbox
	 */
	public void resetCurrent(String extension)
	{
		get(extension).resetCurrentMessage() ;
	}
	
	/**
	 * Moves the current message pointer to the next message in the old queue.
	 * @param extension the extension number of the mailbox
	 */
	public void advanceCurrent(String extension)
	{
		get(extension).advanceCurrentMessage() ;
	}
}
