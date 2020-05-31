/**
   Connects a phone to the mail center. This class keeps
   track of the state of a connection, since the phone
   itself is just a source of individual key pressed.
*/
public class ControlHub
{
	private final MailCenter center ;
	private static String currentRecording ;
	private static String accumulatedKeys ;
	private static String extension ;
	private final Telephone phone ;
	private static int state ;
	
	private static final int IDLE					= 0 ;
	private static final int CONNECTED 				= 1 ;
	private static final int RECORDING 				= 2 ;
	private static final int LOG_IN					= 3 ;
	private static final int MAILBOX_MENU 			= 4 ;
	private static final int OLD_MESSAGE_MENU 		= 5 ;
	private static final int NEW_MESSAGE_MENU 		= 6 ;
	private static final int OWNER_CHANGE_PASSWORD 	= 7 ;
	private static final int GREETING_MENU 			= 8 ;
	private static final int SWITCH_GREETING 		= 9 ;
	private static final int RECORD_GREETING 		= 10 ;
	private static final int DELETE_GREETING 		= 11 ;
	private static final int ADMIN_MENU				= 12 ;
	private static final int CREATE_EXTENSION		= 13 ;
	private static final int CREATE_PASSWORD		= 14 ;
	private static final int FIND_EXTENSION			= 15 ;
	private static final int ADMIN_CHANGE_PASSWORD	= 16 ;
	
	private static final String MAILBOX_MENU_TEXT = 
			"\nMAILBOX MENU:\n"
			+ "Enter 1 to retrieve your messages\n"
			+ "Enter 2 to change your password\n"
			+ "Enter 3 to manage your greetings\n" ;
	
	private static final String OLD_MESSAGE_MENU_TEXT = 
			"\nMESSAGE MENU:\n"
			+ "Enter 1 to listen to this message again\n"
			+ "Enter 2 to delete this message\n"
			+ "Enter 3 to listen to next message\n"
			+ "Enter 4 to back to the previous menu\n" ;
	
	private static final String NEW_MESSAGE_MENU_TEXT = 
			"\nMESSAGE MENU:\n"
			+ "Enter 1 to listen to this message again\n"
			+ "Enter 2 to save this message\n"
			+ "Enter 3 to delete this message\n"
			+ "Enter 4 to back to the previous menu\n" ;
	
	private static final String GREETING_MENU_TEXT = 
			"\nGREETING MENU:\n"
			+ "Enter 1 to switch your greeting\n"
			+ "Enter 2 to record a new greeting\n"
			+ "Enter 3 to delete one of your greetings\n"
			+ "Enter 4 to back to the previous menu\n" ;
	
	private static final String ADMIN_MENU_TEXT = 
			"\nADMINISTRATOR MENU:\n"
			+ "Enter 1 to create a new mailbox\n"
			+ "Enter 2 to change a mailbox password\n"
			+ "Enter 3 to reset all mailboxes passwords\n" ;
	
	/**
	 * Construct a ControlHub object.
	 * @param c a MailCenter object
	 * @param p a Telephone object
	 */
	public ControlHub(MailCenter c, Telephone p)
	{
		center = c ;
		phone = p ;
		resetConnection() ;
		phone.speak("Welcome to Le Duy Vu's voicemail system\nPlease log in as an administrator"
				+ " first to create the first mailbox to test the system\nReady to receive command") ;
	}
	
	/**
	 * Resets the connection to the IDLE state and says welcome.
	 */
	private void resetConnection()
	{
		currentRecording = "" ;
		accumulatedKeys = "" ;
		extension = "" ;
		state = IDLE ;
		phone.speak("\nWelcome to Le Duy Vu's voicemail system. Ready to receive command") ;
	}
	
	/**
	 * Runs when the user hangs up the phone, records new message if applicable, then resets connection.
	 */
	public void hangUp()
	{
		if (state == RECORDING)
			if (currentRecording != "")
				center.recordNewMessage(extension, currentRecording) ;
		resetConnection() ;
	}
	
	/**
	 * Records the input voice if applicable.
	 * @param voice voice spoken by the user
	 */
	public void record(String voice)
	{
		if (state == RECORDING || state == RECORD_GREETING)
			currentRecording += voice + "\n" ;
		else
			phone.speak("Please follow instruction!\n") ;
	}
	
	/**
	 * Responds when the user presses a key on the phone.
	 * @param key the single key pressed by the user
	 */
	public void dial(String key)
	{
		switch (state)
		{
			case IDLE:
				determine(key) ;
				break ;
			case CONNECTED:
				connect(key) ;
				break ;
			case LOG_IN:
				logIn(key) ;
				break ;
			case MAILBOX_MENU:
				mailboxMenu(key) ;
				break ;
			case OLD_MESSAGE_MENU:
				oldMessageMenu(key) ;
				break ;
			case NEW_MESSAGE_MENU:
				newMessageMenu(key) ;
				break ;
			case OWNER_CHANGE_PASSWORD:
				changePassword(key) ;
				break ;
			case GREETING_MENU:
				greetingMenu(key) ;
				break ;
			case SWITCH_GREETING:
				switchGreeting(key) ;
				break ;
			case RECORD_GREETING:
				recordGreeting(key) ;
				break ;
			case DELETE_GREETING:
				deleteGreeting(key) ;
				break ;
			case ADMIN_MENU:
				adminMenu(key) ;
				break ;
			case CREATE_EXTENSION:
				createExtension(key) ;
				break ;
			case CREATE_PASSWORD:
				createPassword(key) ;
				break ;
			case FIND_EXTENSION:
				prepAdminChangePassword(key) ;
				break ;
			case ADMIN_CHANGE_PASSWORD:
				adminChangePassword(key) ;
				break ;
		}
	}
	
	/**
	 * Determines which type of user is using the phone.
	 * @param key the single key pressed by the user
	 */
	private void determine(String key)
	{
		if (!key.equals("#"))
		{
			accumulatedKeys += key ;
		}
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("\nPlease enter key before #") ;
				return;
			}
			
			if (accumulatedKeys.equals("123456789"))
			{
				state = CONNECTED ;
				phone.speak("\nWelcome to SJSU. Please enter the extension number you want to reach") ;
			}
			else if (center.checkMailbox(accumulatedKeys))
			{
				state = LOG_IN ;
				extension = accumulatedKeys ;
				phone.speak("\nWelcome to your mailbox. Please enter your password") ;
			}
			else if (accumulatedKeys.equals("21120109"))
			{
				state = ADMIN_MENU ;
				phone.speak("\nWelcome, admin\n" + ADMIN_MENU_TEXT) ;
			}
			else
				phone.speak("\nThe system doesn't understand your command. Please try again\n") ;
			
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Tries to connect the caller with the specified mailbox.
	 * @param key the single key pressed by the user
	 */
	private void connect(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("\nPlease enter key before #") ;
				return;
			}
			
			extension = accumulatedKeys ;
			
			if (center.checkMailbox(accumulatedKeys))
			{
				if (center.newSize(extension) < Mailbox.MAXIMUM_MESSAGE)
				{
					state = RECORDING ;
					phone.speak("\n" + center.getCurrentGreeting(extension)) ;
				}
				else
				{
					phone.speak("\nThis mailbox can't receive any new message. Please come back at another time\n") ;
					resetConnection() ;
				}
			}
			else
				phone.speak("\nThis mailbox does not exist. Please try a different one!\n") ;
			
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Tries to log in as the mailbox owner.
	 * @param key the single key pressed by the user
	 */
	private void logIn(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (center.checkPassword(extension, accumulatedKeys))
			{
				state = MAILBOX_MENU ;
				phone.speak("\nLog in successfully\n" + MAILBOX_MENU_TEXT) ;
			}
			else
				phone.speak("\nIncorrect password. Try again!\n") ;
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Responds to the owner's choice from mailbox menu.
	 * @param key the single key pressed by the user
	 */
	private void mailboxMenu(String key)
	{
		switch (key)
		{
			case "1":
				if (center.oldSize(extension) == 0 && center.newSize(extension) == 0)
					phone.speak("\nThere is no message in your mailbox. Please come back at another time\n"
							+ MAILBOX_MENU_TEXT) ;
				else if (center.newSize(extension) == 0)
				{
					state = OLD_MESSAGE_MENU ;
					center.resetCurrent(extension) ;
					phone.speak("\nThere is no new message. You have " + center.oldSize(extension) + " old message(s).\n\n"
							+ "First old message:\n\n" + center.oldFront(extension) + OLD_MESSAGE_MENU_TEXT) ;
				}
				else
				{
					state = NEW_MESSAGE_MENU ;
					phone.speak("\nYou have " + center.newSize(extension) + " new message(s).\n\n"
							+ "First new message:\n\n" + center.newFront(extension) + NEW_MESSAGE_MENU_TEXT) ;
				}
				break ;
			case "2":
				state = OWNER_CHANGE_PASSWORD ;
				phone.speak("\nEnter new password followed by the # key") ;
				break ;
			case "3":
				state = GREETING_MENU ;
				phone.speak("\nYou have " + center.getGreetingsCount(extension) + " greeting(s).\n" + GREETING_MENU_TEXT) ;
				break ;
			default:
				phone.speak("Invalid key. Please enter again.\n") ;
		}
	}
	
	/**
	 * Runs when there is no new message but there is old message in the mailbox.
	 * @param key the single key pressed by the user
	 */
	private void oldMessageMenu(String key)
	{
		switch (key)
		{
			case "1":
				if (center.oldFront(extension) != center.currentMessage(extension))
					phone.speak("\n" + center.currentMessage(extension) + OLD_MESSAGE_MENU_TEXT) ;
				else
					phone.speak("\n" + center.oldFront(extension) + OLD_MESSAGE_MENU_TEXT) ;
				break ;
			case "2":
				center.removeOldMessage(extension) ;
				if (center.oldSize(extension) != 0)
					phone.speak("\nMessage deleted successfully\n\n"
							+ "Next old message:\n\n" 
							+ center.currentMessage(extension) + OLD_MESSAGE_MENU_TEXT) ;
				else
				{
					state = MAILBOX_MENU ;
					phone.speak("\nMessage deleted successfully\n\n"
							+ "You have no old message left\n" + MAILBOX_MENU_TEXT);
				}
				break ;
			case "3":
				center.advanceCurrent(extension) ;
				phone.speak("\nNext old message:\n" + center.currentMessage(extension) + OLD_MESSAGE_MENU_TEXT) ;
				break ;
			case "4":
				state = MAILBOX_MENU ;
				phone.speak(MAILBOX_MENU_TEXT) ;
				break ;
			default:
				phone.speak("Invalid key. Please enter again.\n") ;
		}
	}
	
	/**
	 * Runs when there is new message in the mailbox.
	 * @param key the single key pressed by the user
	 */
	private void newMessageMenu(String key)
	{
		switch (key)
		{
			case "1":
				phone.speak("\n" + center.newFront(extension) + NEW_MESSAGE_MENU_TEXT) ;
				break ;
			case "2":
				center.saveNewMessage(extension) ;
				if (center.newSize(extension) != 0)
					phone.speak("\nMessage saved successfully\n\n"
							+ "Next new message:\n\n" + center.newFront(extension) + NEW_MESSAGE_MENU_TEXT) ;
				else
				{
					state = MAILBOX_MENU ;
					phone.speak("\nMessage saved successfully\n\n"
							+ "You have no new message left\n" + MAILBOX_MENU_TEXT);
				}
				break ;
			case "3":
				center.removeNewMessage(extension) ;
				if (center.newSize(extension) != 0)
					phone.speak("\nMessage deleted successfully\n\n"
							+ "Next new message:\n\n" + center.newFront(extension) + NEW_MESSAGE_MENU_TEXT) ;
				else
				{
					state = MAILBOX_MENU ;
					phone.speak("\nMessage deleted successfully\n\n"
							+ "You have no new message left\n" + MAILBOX_MENU_TEXT);
				}
				break ;
			case "4":
				state = MAILBOX_MENU ;
				phone.speak(MAILBOX_MENU_TEXT) ;
				break ;
			default:
				phone.speak("Invalid key. Please enter again.\n") ;
		}
	}
	
	/**
	 * Changes password for the owner.
	 * @param key the single key pressed by the user
	 */
	private void changePassword(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("Password can't be empty. Please enter new password again.\n") ;
				return;
			}
			
			center.setPassword(extension, accumulatedKeys) ;
			phone.speak("\nPassword changed successfully\n" + MAILBOX_MENU_TEXT) ;
			state = MAILBOX_MENU ;
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Responds to the owner's choice from greeting menu.
	 * @param key the single key pressed by the user
	 */
	private void greetingMenu(String key)
	{
		int greetingNum = center.getGreetingsCount(extension) ;
		
		switch (key)
		{
			case "1":
				prepSwitchGreeting(greetingNum) ;
				break ;
			case "2":
				prepRecordGreeting(greetingNum) ;
				break ;
			case "3":
				prepDeleteGreeting(greetingNum) ;
				break ;
			case "4":
				state = MAILBOX_MENU ;
				phone.speak(MAILBOX_MENU_TEXT) ;
				break ;
			default:
				phone.speak("Invalid key. Please enter again.\n") ;
		}
	}
	
	/**
	 * Checks whether the owner can switch their default greeting.
	 * @param num the total number of greetings the owner has
	 */
	private void prepSwitchGreeting(int num)
	{
		if (num == 1)
			phone.speak("\nYou only have 1 greeting. Please record a new one first to switch\n" + GREETING_MENU_TEXT) ;
		else
		{
			state = SWITCH_GREETING ;
			for (int i = 1; i <= Mailbox.MAXIMUM_GREETING; i++)
				if (center.getSpecificGreeting(extension, i) != null)
					phone.speak("\nGreeting " + i + ":\n" + center.getSpecificGreeting(extension, i)) ;
			phone.speak("Enter the number of the greeting you want to switch to:");
		}
	}
	
	/**
	 * Switches default greeting for the owner.
	 * @param key the single key pressed by the user
	 */
	private void switchGreeting(String key)
	{
		int k = Integer.parseInt(key) ;
		if (1 <= k && k <= Mailbox.MAXIMUM_GREETING)
		{
			if (center.getSpecificGreeting(extension, k) == null)
			{
				phone.speak("\nThe greeting you choose doesn't exist. Please choose a different one\n") ;
				return;
			}
			
			center.switchGreeting(extension, k) ;
			phone.speak("\nGreeting switched succesfully\n" + GREETING_MENU_TEXT) ;
			state = GREETING_MENU ;
		}
		else
			phone.speak("Invalid key. Please enter again.\n") ;
	}
	
	/**
	 * Checks whether the owner can record a greeting.
	 * @param num the total number of greetings the owner has
	 */
	private void prepRecordGreeting(int num)
	{
		if (num == 3)
			phone.speak("\nYou have reached maximum of 3 greetings. To record a new one, "
					+ "please delete an existing greeting first\n" + GREETING_MENU_TEXT) ;
		else
		{
			state = RECORD_GREETING ;
			phone.speak("\nRecord your greeting, then press #") ;
		}
	}
	
	/**
	 * Records a new greeting for the owner.
	 * @param key the single key pressed by the user
	 */
	private void recordGreeting(String key)
	{
		if (key.equals("#"))
		{
			if (currentRecording == "")
			{
				phone.speak("Greeting can't be empty. Please record your greeting again.\n") ;
				return;
			}
			
			center.recordGreeting(extension, currentRecording) ;
			currentRecording = "" ;
			state = GREETING_MENU ;
			phone.speak("\nGreeting recorded succesfully\n" + GREETING_MENU_TEXT) ;
		}
	}
	
	/**
	 * Checks whether the owner can deletes a greeting.
	 * @param num the total number of greetings the owner has
	 */
	private void prepDeleteGreeting(int num)
	{
		if (num == 1)
			phone.speak("\nYou can't delete your only greeting. To delete this greeting, "
					+ "please record a new one first\n" + GREETING_MENU_TEXT) ;
		else
		{
			state = DELETE_GREETING ;
			for (int i = 1; i <= Mailbox.MAXIMUM_GREETING; i++)
				if (center.getSpecificGreeting(extension, i) != null)
					phone.speak("\nGreeting " + i + ":\n" + center.getSpecificGreeting(extension, i)) ;
			phone.speak("Enter the number of the greeting you want to delete:");
		}
	}
	
	/**
	 * Deletes a non-default greeting for the user.
	 * @param key the single key pressed by the user
	 */
	private void deleteGreeting(String key)
	{
		int k = Integer.parseInt(key) ;
		if (1 <= k && k <= Mailbox.MAXIMUM_GREETING)
		{
			if (k == center.getCurrentGreetingNumber(extension))
			{
				phone.speak("\nYou can't delete your currently used greeting. To delete this"
						+ " greeting, please switch to another one first\n" + GREETING_MENU_TEXT) ;
				state = GREETING_MENU ;
				return;
			}
			
			if (center.getSpecificGreeting(extension, k) == null)
			{
				phone.speak("\nThe greeting you choose doesn't exist. Please choose a different one\n") ;
				return;
			}
			
			center.deleteGreeting(extension, k) ;
			phone.speak("\nGreeting deleted succesfully\n" + GREETING_MENU_TEXT) ;
			state = GREETING_MENU ;
		}
		else
			phone.speak("Invalid key. Please enter again.\n") ;
	}
	
	/**
	 * Responds to the owner's choice from admin menu.
	 * @param key the single key pressed by the user
	 */
	private void adminMenu(String key)
	{
		switch (key)
		{
			case "1":
				state = CREATE_EXTENSION ;
				phone.speak("\nEnter the extension number for the new mailbox:") ;
				break ;
			case "2":
				state = FIND_EXTENSION ;
				phone.speak("\nEnter the mailbox extension you want to change password:") ;
				break ;
			case "3":
				if (center.size() > 0)
				{
					center.resetAllPassword() ;
					phone.speak("\nAll mailboxes passwords have been set to default, "
							+ "which are the same as their extension number\n" + ADMIN_MENU_TEXT) ;
				}
				else
					phone.speak("\nThere is no mailbox in the system. Please create a mailbox first\n" + ADMIN_MENU_TEXT) ;
				break ;
			default:
				phone.speak("Invalid key. Please enter again.\n") ;
		}
	}
	
	/**
	 * Creates a new extension that haven't existed in the system.
	 * @param key the single key pressed by the user
	 */
	private void createExtension(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("\nExtension can't be empty. Please enter again\n") ;
				return;
			}
			
			if (center.checkMailbox(accumulatedKeys))
			{
				phone.speak("\nThis extension already existed. Please create a different extension\n" + ADMIN_MENU_TEXT) ;
				state = ADMIN_MENU ;
			}
			else
			{
				state = CREATE_PASSWORD ;
				extension = accumulatedKeys ;
				phone.speak("\nEnter the password for the new mailbox:") ;
			}
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Creates the password for the new extension.
	 * @param key the single key pressed by the user
	 */
	private void createPassword(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("\nPassword can't be empty. Please enter the password again\n") ;
				return;
			}
			
			center.add(extension, accumulatedKeys) ; 
			phone.speak("\nNew mailbox created successfully\n" + ADMIN_MENU_TEXT) ;
			state = ADMIN_MENU ;
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Chooses the mailbox that got password changed.
	 * @param key the single key pressed by the user
	 */
	private void prepAdminChangePassword(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (center.checkMailbox(accumulatedKeys))
			{
				state = ADMIN_CHANGE_PASSWORD ;
				extension = accumulatedKeys ;
				phone.speak("\nEnter new password for this mailbox:") ;
			}
			else
				phone.speak("\nThis mailbox does not exist. Please try a different one!\n") ;
			accumulatedKeys = "" ;
		}
	}
	
	/**
	 * Changes the password of the chosen mailbox.
	 * @param key the single key pressed by the user
	 */
	private void adminChangePassword(String key)
	{
		if (!key.equals("#"))
			accumulatedKeys += key ;
		else
		{
			if (accumulatedKeys == "")
			{
				phone.speak("\nPassword can't be empty. Please enter new password again\n") ;
				return;
			}
			
			center.setPassword(extension, accumulatedKeys) ;
			phone.speak("\nPassword changed succesfully\n" + ADMIN_MENU_TEXT) ;
			state = ADMIN_MENU ;
			accumulatedKeys = "" ;
		}
	}
}