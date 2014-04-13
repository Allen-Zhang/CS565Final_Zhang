package cs565.finals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class DatePicker extends JPanel {

	private String year;  // Selected year
	private String month;  // Selected month
	private String day;  // Selected day
	
	private Container container;
	private String parten;
	private int length = 100;  // Length of DateChooser

	private static final int WIDTH = 230; 
	private static final int HEIGHT = 210; 
	
	private JTextField textF_DATE;
	private DateChooserButton button_CHOOSE;
	private Calendar calendar = Calendar.getInstance();
	
	public DatePicker(Container container) {
		this.container = container;
		this.parten = "yyyy-MM-dd";
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public DatePicker(Container container, int length) {
		this.container = container;
		this.parten = "yyyy-MM-dd";
		this.length = length;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public DatePicker(Container container, String partten) {
		this.container = container;
		this.parten = partten;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public DatePicker(Container container, String partten, int length) {
		this.container = container;
		this.parten = partten;
		this.length = length;
		try {
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDay() {
		return day;
	}
	
	public int getIntDate() {
		return Integer.parseInt(year+month+day);
	}

	private void init() throws Exception {
		this.setLayout(new GridBagLayout());
		textF_DATE = new JTextField();
		button_CHOOSE = new DateChooserButton(" ¨‹ ");
		textF_DATE.setEditable(false);
		textF_DATE.setBackground(Color.white);
		textF_DATE.setToolTipText("Click ¨‹ button to choose date");
		button_CHOOSE.setToolTipText("Click to choose date");

		// Add listener for choose date button
		button_CHOOSE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatePicker.this.buttonChooseActionPerformed(e);
			}
		});
		
		// Initial year, month, and day to be current date
		this.year = String.valueOf(calendar.get(Calendar.YEAR));
	    int m = calendar.get(Calendar.MONTH) + 1;
		this.month = (m < 10) ? "0"+String.valueOf(m) : String.valueOf(m);
	    int d = calendar.get(Calendar.DATE);
		this.day = (d < 10) ? "0"+String.valueOf(d) : String.valueOf(d);
	    
	    // Format the date
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parten);
		this.setText(simpleDateFormat.format(date));
		this.add(textF_DATE, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), this.length, 0));
		this.add(button_CHOOSE, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
	}

	public void setToolTipText(String text) {
		textF_DATE.setToolTipText(text);
		button_CHOOSE.setToolTipText(text);
	}

	public void buttonChooseActionPerformed(ActionEvent e) {
		Rectangle r = textF_DATE.getBounds();
		Point pOnScreen = textF_DATE.getLocationOnScreen();
		Point result = new Point(pOnScreen.x, pOnScreen.y + r.height);
		Point pcontainer = container.getLocation();
		int offsetX = (pOnScreen.x - WIDTH) - (pcontainer.x + container.getWidth());
		int offsetY = (pOnScreen.y + r.height + HEIGHT)
				- (pcontainer.y + container.getHeight());
		if (offsetX > 0) {
			result.x -= offsetX;
		}
		if (offsetY > 0) {
			result.y -= HEIGHT + r.height;
		}
		JDialog dateFrame = new JDialog();
		dateFrame.setModal(false);
		dateFrame.setUndecorated(true);
		dateFrame.setLocation(result);
		dateFrame.setSize(WIDTH, HEIGHT);

		dateFrame.addWindowListener(new WindowAdapter() {
			// Deactivate DateChooser when click position not in the Calendar field  
			public void windowDeactivated(WindowEvent e) {
				JDialog f = (JDialog) e.getSource();
				f.dispose();
			}
		});
		DatePanel datePanel = new DatePanel(dateFrame, parten);
		dateFrame.getContentPane().setLayout(new BorderLayout());
		dateFrame.getContentPane().add(datePanel);
		dateFrame.setVisible(true);
	}

	public String getText() {
		return this.textF_DATE.getText();
	}

	public void setText(String text) {
		this.textF_DATE.setText(text);
	}

	public JTextField getTextDate() {
		return textF_DATE;
	}

	class DatePanel extends JPanel implements MouseListener, ChangeListener {

		int startYear = 1970;  // Set the start year
		int lastYear = 2050;  // Set the end year

		Color backGroundColor = Color.gray;
		/*----- Color For The Calendar -----*/
		Color palletTableColor = Color.white;  // Calendar color
		Color weekFontColor = Color.blue;  // Font color for weekday
		Color dateFontColor = Color.black;  // Font color date
		Color weekendFontColor = Color.red;  // Font color for weekend
		Color moveButtonColor = Color.cyan;  // Background color for chosen day
		Color todayBtnColor = Color.lightGray; // Background color for today
		/*----- Color For The Control Bar -----*/
		Color controlLineColor = Color.lightGray;  // Background color for control bar
		Color controlTextColor = Color.white;  // Font color on control bar

		JSpinner yearSpin;
		JSpinner monthSpin;
		JButton[][] daysButton = new JButton[6][7];

		JDialog f;
		String pattern;
		JPanel dayPanel = new JPanel(); 
		JPanel yearPanel = new JPanel();

		public DatePanel(JDialog target, String pattern) {
			super();
			this.f = target;
			this.pattern = pattern;

			setLayout(new BorderLayout());
			setBorder(new LineBorder(backGroundColor, 2));
			setBackground(backGroundColor);
			initButton();  // initiate calendar buttons
			createYearAndMonthPanal(); 
			this.flushWeekAndDayPanal(calendar); 
			this.setLayout(new BorderLayout());
			this.add(yearPanel, BorderLayout.NORTH);
			this.add(dayPanel, BorderLayout.CENTER);
		}

		private void initButton() {
			int actionCommandId = 1;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					JButton numberButton = new JButton();
					numberButton.setBorder(BorderFactory.createEmptyBorder());
					numberButton.setHorizontalAlignment(SwingConstants.CENTER);
					numberButton.setActionCommand(String
							.valueOf(actionCommandId));
					// Add listener for number button
					numberButton.addMouseListener(this);

					numberButton.setBackground(palletTableColor);
					numberButton.setForeground(dateFontColor);
					numberButton.setText(String.valueOf(actionCommandId));
					numberButton.setPreferredSize(new Dimension(25, 25));
					daysButton[i][j] = numberButton;
					actionCommandId++;
				}
			}
		}

		private Calendar getNowCalendar() {
			Calendar result = Calendar.getInstance();
			return result;
		}

		private Date getSelectDate() {
			return calendar.getTime();
		}

		private void createYearAndMonthPanal() {
			Calendar c = getNowCalendar();
			int currentYear = c.get(Calendar.YEAR);
			int currentMonth = c.get(Calendar.MONTH) + 1;
			
			yearSpin = new JSpinner(new SpinnerNumberModel(
					currentYear, startYear, lastYear, 1));
			monthSpin = new JSpinner(new SpinnerNumberModel(
					currentMonth, 1, 12, 1));

			yearPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			yearPanel.setBackground(controlLineColor);

			JLabel yearLabel = new JLabel("Year");
			yearLabel.setForeground(controlTextColor);
			yearPanel.add(yearLabel);
			
			yearSpin.setPreferredSize(new Dimension(48, 20));
			yearSpin.setName("Year");
			yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
			yearSpin.addChangeListener(this);
			yearPanel.add(yearSpin);

			JLabel monthLabel = new JLabel("    Month");
			monthLabel.setForeground(controlTextColor);
			yearPanel.add(monthLabel);

			monthSpin.setPreferredSize(new Dimension(35, 20));
			monthSpin.setName("Month");
			monthSpin.addChangeListener(this);
			yearPanel.add(monthSpin);
		}

		private void flushWeekAndDayPanal(Calendar c) {
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.setFirstDayOfWeek(0);
			int firstdayofWeek = c.get(Calendar.DAY_OF_WEEK);
			int lastdayofWeek = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			int today = getNowCalendar().get(Calendar.DAY_OF_MONTH);
			String colname[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
			dayPanel.setFont(new java.awt.Font("Serif", Font.PLAIN, 12));
			dayPanel.setLayout(new GridBagLayout());
			dayPanel.setBackground(Color.white);
			JLabel cell;
			// Set the date cell
			for (int i = 0; i < 7; i++) {
				cell = new JLabel(colname[i]);
				cell.setHorizontalAlignment(JLabel.CENTER);
				cell.setPreferredSize(new Dimension(28, 25));
				if (i == 0 || i == 6) {
					cell.setForeground(weekendFontColor);
				} else {
					cell.setForeground(weekFontColor);
				}
				dayPanel.add(cell, new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 2, 0, 2), 0, 0));
			}
			int actionCommandId = 1;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {

					JButton numberButton = daysButton[i][j];
					actionCommandId = Integer.parseInt(numberButton
							.getActionCommand());
					if (actionCommandId == today) {
						numberButton.setBackground(todayBtnColor);
					}
					if ((actionCommandId + firstdayofWeek - 2) % 7 == 6
							|| (actionCommandId + firstdayofWeek - 2) % 7 == 0) {
						numberButton.setForeground(weekendFontColor);
					} else {
						numberButton.setForeground(dateFontColor);
					}
					if (actionCommandId <= lastdayofWeek) {
						int y = 0;
						if ((firstdayofWeek - 1) <= (j + firstdayofWeek - 1) % 7) {
							y = i + 1;
						} else {
							y = i + 2;
						}
						dayPanel.add(numberButton, new GridBagConstraints((j
								+ firstdayofWeek - 1) % 7, y, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		}

		private int getSelectedYear() {
			return ((Integer) yearSpin.getValue()).intValue();
		}

		private int getSelectedMonth() {
			return ((Integer) monthSpin.getValue()).intValue();
		}

		public void stateChanged(ChangeEvent e) {
			JSpinner source = (JSpinner) e.getSource();
			if (source.getName().equals("Year")) {
				calendar.set(Calendar.YEAR, getSelectedYear());
				dayPanel.removeAll();
				this.flushWeekAndDayPanal(calendar);
				dayPanel.revalidate();
				dayPanel.updateUI();
				return;
			}
			if (source.getName().equals("Month")) {
				calendar.set(Calendar.MONTH, getSelectedMonth() - 1);
				dayPanel.removeAll();
				this.flushWeekAndDayPanal(calendar);
				dayPanel.revalidate();
				dayPanel.updateUI();
				return;
			}
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				JButton source = (JButton) e.getSource();
				
				// Assign user selected date value to year, month, and day variable
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(source.getText()));
				int d = calendar.get(Calendar.DAY_OF_MONTH);
				DatePicker.this.day = (d < 10) ? "0"+String.valueOf(d) : String.valueOf(d);
				Date selectDate = this.getSelectDate();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				DatePicker.this.setText(simpleDateFormat.format(selectDate));
				
				DatePicker.this.year  = String.valueOf(calendar.get(Calendar.YEAR));
				int m = calendar.get(Calendar.MONTH) + 1;
				DatePicker.this.month = (m < 10) ? "0"+String.valueOf(m) : String.valueOf(m);
//				System.out.println("Year: " + year + " Month: " + month + " Day: " + day);
				f.dispose();
			}
		}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {
			JButton jbutton = (JButton) e.getSource();
			jbutton.setBackground(moveButtonColor);
		}

		public void mouseExited(MouseEvent e) {
			JButton jbutton = (JButton) e.getSource();
			int comm = Integer.parseInt(jbutton.getActionCommand());
			int today = getNowCalendar().get(Calendar.DAY_OF_MONTH);
			if (comm == today) {
				jbutton.setBackground(todayBtnColor);
			} else {
				jbutton.setBackground(palletTableColor);
			}
		}
	}

	class DateChooserButton extends JButton {
		public DateChooserButton(String text) {
			super(text);
		}

		public Insets getInsets() {
			return new Insets(4, 2, 0, 2);
		}
	}
}