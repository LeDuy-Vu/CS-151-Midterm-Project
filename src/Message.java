/**
 * A message from the caller for the mailbox owner.
 */
public class Message
{
	private final String content ;
	
	/**
	 * Constructs a message with nonempty content.
	 * @param content
	 * @precondition content.length() > 0
	 */
	public Message(String content)
	{
		assert(content.length() > 0) : "Message can't be empty!" ;
		this.content = content ;
	}
	
	/**
	 * Gets the content of the message.
	 * @return the message content
	 */
	public String getContent()
	{
		return content;
	}
}
