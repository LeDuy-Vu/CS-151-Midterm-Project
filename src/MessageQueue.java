/**
 * A circular array collection of messages.
 */
public class MessageQueue
{
	private final Message[] messageList ;
	private int count ;
	private int head ;
	private int tail ;
	private int current ;
	
	private static final int OLD_QUEUE_CAPACITY = 10 ;
	
	/**
	 * Constructs an empty message queue with assumably really long capacity.
	 */
	public MessageQueue()
	{
		messageList = new Message[OLD_QUEUE_CAPACITY] ;
		count = head = tail = current = 0 ;
	}
	
	/**
	 * Constructs an empty message queue with supplied capacity.
	 * @param capacity the maximum capacity of the queue
	 * @precondition capacity > 0
	 */
	public MessageQueue(int capacity)
	{
		assert(capacity > 0) : "Capacity of the queue must be bigger than 0!" ;
		messageList = new Message[capacity] ;
		count = head = tail = current = 0 ;
	}
	
	/**
	 * Gets the total number of messages in the queue.
	 * @return the number of messages
	 */
	public int size()
	{
		return count;
	}
	
	/**
	 * Checks whether the queue is full
	 * @return true if the queue is full
	 */
	public boolean isFull()
	{
		return count == messageList.length;
	}
	
	/**
	 * Gets the message at the front of the queue.
	 * @return the first message in the queue
	 * @precondition size() > 0
	 */
	public Message front()
	{
		assert(size() > 0) : "There is no message!" ;
		return messageList[head];
	}
	
	/**
	 * Gets the current message of the queue.
	 * @return the current message
	 * @precondition size() > 0
	 */
	public Message current()
	{
		assert(size() > 0) : "There is no message!" ;
		return messageList[current];
	}
	
	/**
	 * Resets the current message pointer back to the default position at head.
	 */
	public void resetCurrent()
	{
		current = head ;
	}
	
	/**
	 * Moves the current message pointer to the next message in the queue.
	 */
	public void advanceCurrent()
	{
		if (size() == 0)
		{
			current = (current + 1) % messageList.length ;
			return;
		}
		
		do
			current = (current + 1) % messageList.length ;
		while (messageList[current] == null) ;
	}
	
	/**
	 * Appends a message at the end of the queue.
	 * @param message the message to be added
	 * @precondition !isFull()
	 */
	public void add(Message message)
	{
		assert(!isFull()) : "The message queue is already full!" ;
		messageList[tail] = message ;
		count++ ;
		tail = (tail + 1) % messageList.length ;
	}
	
	/**
	 * Removes the message at the front of the queue.
	 * @return the message that has been removed from the queue
	 * @precondition size() > 0
	 */
	public Message remove()
	{
		assert(size() > 0) : "There is no message to be removed!" ;
		
		if (current != head)
		{
			Message temp = messageList[head] ;
			messageList[head] = messageList[current] ;
			messageList[current] = temp ;
		}
		
		Message m = messageList[head] ;
		messageList[head] = null ;
		head = (head + 1) % messageList.length ;
		count-- ;
		advanceCurrent() ;
		return m;
	}
}
