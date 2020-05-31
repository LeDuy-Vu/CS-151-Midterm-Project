/**
 * This program tests the mail system. A single phone
 * communicates with the program through System.in/System.out.
 */
public class VoicemailSystemTester
{
	public static void main(String[] args)
	{
		MailCenter c = new MailCenter() ;
		Telephone p = new Telephone() ;
		ControlHub h = new ControlHub(c, p) ;
		p.startSystem(h) ;
	}
}