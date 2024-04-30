package utilerias;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Date;
import java.util.Calendar;

/**
 * Permite a los usuarios seleccionar una fecha y colocarla con el formato correcto en el 
 */
public class Calendario extends JDialog implements ActionListener {
	private String[] months = {"Enero","Febrero", "Marzo","Abril", "Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
	private JButton[] days = null;
	private JLabel monthLabel = null;
	private Calendar calendar = null;
	private JPanel mainPanel = null;
	private JTable tabla;
	private Component returnCmp;

	/**
	 * Constructor.
	 */
	public Calendario() {
		super();
		this.setModal(true);
                this.setAlwaysOnTop(true);                
		initialize();
	}


	/**
	 * cacha los eventos
	 * @param e java.awt.event.ActionEvent El evento
	 */
	public void actionPerformed(ActionEvent e) {
		String text;
		if (e.getActionCommand().equals("D")){
			text = ((JButton) e.getSource()).getText();
			if (text.length() > 0){
				this.returnDate(text);
			} else {
				Toolkit.getDefaultToolkit().beep();
			}	
		} else {
			this.roll(e.getActionCommand());
		
		}
		
	}
	/**
	 * Pinta la ventana con el mes seleccionado
	 */
	private void caption() {
		Calendar cal = this.getCalendar();
		int startPos;
		int currentMonth = cal.get(Calendar.MONTH);
		mainPanel.setVisible(false);
		monthLabel.setText(months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		startPos = cal.get(Calendar.DAY_OF_WEEK);
			
		for (int i = startPos - 1; i < days.length; i++) {
			days[i].setText(String.valueOf(cal.get(Calendar.DATE)));
			cal.roll(Calendar.DATE, true);
			if (cal.get(Calendar.DATE) == 1) {
				for (int j = i + 1; j < days.length; j++) {
					days[j].setText("");
				}
				break;
			}
		}
		for (int h = 0; h < startPos - 1; h++) {
			if (cal.get(Calendar.DATE) > 25 ) {
				days[h].setText(String.valueOf(cal.get(Calendar.DATE)));
				cal.roll(Calendar.DATE, true);
			} else {
				days[h].setText("");
			}
		}
	
		this.setCalendar(cal);
		mainPanel.setVisible(true);
	}

	/**
	 * Obtiene la fecha actual
	 * @return java.util.Calendar
	 */
	public java.util.Calendar getCalendar() {
		if (this.calendar == null){
			calendar = Calendar.getInstance();
		}
		return calendar;
	}

	/**
	 * Crea la interfaz
	 */
	private void initialize() {
		JButton jb;
		mainPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
	
		monthLabel = new JLabel(months[9] + " 1953");
		northPanel.setLayout(new FlowLayout());
		northPanel.add(monthLabel);
	
		centerPanel.setLayout(new GridLayout(5,7));
		days = new JButton[35];
		for (int i = 0; i <35; i++){
			jb = new JButton(String.valueOf(i));
			jb.setSize(25,25);
			jb.setBorder(new EmptyBorder(1,1,1,1));
			jb.setFocusPainted(false);
			jb.setActionCommand("D");
			jb.addActionListener(this);
			days[i] = jb;
			centerPanel.add(jb);	
		}
		southPanel.setLayout(new FlowLayout());
		southPanel.add(this.makeButton("<<"));
		southPanel.add(this.makeButton("<"));
		southPanel.add(this.makeButton(">"));
		southPanel.add(this.makeButton(">>"));
	
	
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(northPanel, "North");
		mainPanel.add(centerPanel,"Center");
		mainPanel.add(southPanel, "South");
	
		this.getContentPane().add(mainPanel);
		this.setSize(250,250);
		
		caption();
	}

	/**
	 * Crea un boton con la fecha recibida
	 * @return java.lang.String
	 */
	private JButton makeButton(String caption) {
		JButton jb = new JButton(caption);
		jb.setSize(25,25);
		jb.setBorder(new EmptyBorder(1,4,1,4));
		jb.setFocusPainted(false);
		jb.setActionCommand(caption);
		jb.addActionListener(this);
		return jb;	
	}

	/**
	 * Cambia la fecha al dia seleccionado y oculta el dialogo
	 * @param day java.lang.String
	 */
	private void returnDate(String day) {
	
		this.getCalendar().set(this.getCalendar().get(Calendar.YEAR),this.getCalendar().get(Calendar.MONTH),Integer.parseInt(day),12,0);		
		this.setVisible(false);
		
	}

	/**
	 * verifica la direccion en la que debe mover la fecha
	 * @param direction java.lang.String
	 */
	private void roll(String direction) {
		int field;
		if (direction.equals(">>")) calendar.roll(Calendar.YEAR,true);
		if (direction.equals(">")) calendar.roll(Calendar.MONTH,true);
		if (direction.equals("<<")) calendar.roll(Calendar.YEAR,false);
		if (direction.equals("<")) calendar.roll(Calendar.MONTH,false);
		caption();
	}

	/**
	 * Se apropia de la fecha recibida
	 * @param newCalendar java.util.Calendar
	 */
	public void setCalendar(java.util.Calendar newCalendar) {
		calendar = newCalendar;
		
	}
	
	public Component getReturnComponent(){
			return returnCmp;
		}
		
		public void setReturnComponent(Component cmp){
			returnCmp = cmp;
			
		}
		
		
}
