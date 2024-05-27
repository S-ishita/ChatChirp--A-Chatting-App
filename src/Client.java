import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client  implements ActionListener {

    JTextField text;
    static JPanel a1;
    static Box vertical=Box.createVerticalBox();
    static JFrame f=new JFrame();
    static DataOutputStream dout;

    Client(){

        f.setLayout(null);
        /*

        green header first panel

         */
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        f.add(p1);


        // back arrow image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2=i1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back=new JLabel(i3);
        back.setBounds(5,20,25,25);
        p1.add(back);
        //back arrow functionality
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // sender dp image
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5=i4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile=new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);

        // video call image
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 =i7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video =new JLabel(i9);
        video.setBounds(300,20,30,30);
        p1.add(video);

        // telephone image
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11=i10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone=new JLabel(i12);
        phone.setBounds(360,20,35,30);
        p1.add(phone);

        // 3 dots more image
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 =i13.getImage().getScaledInstance(12,25,Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert=new JLabel(i15);
        morevert.setBounds(420,23,12,25);
        p1.add(morevert);


        //sender name label
        JLabel name=new JLabel("Peter");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF",Font.BOLD,18));
        p1.add(name);


        //sender online status label
        JLabel status=new JLabel("Active Now");
        status.setBounds(110,37,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF",Font.BOLD,14));
        p1.add(status);

         /*

          text area second panel

         */
        a1=new JPanel();
        a1.setBounds(5,75,440,570);
        f.add(a1);


        //text field for sending message
        text=new JTextField();
        text.setBounds(5,655,310,40);
        text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        f.add(text);

        //send button
        JButton send=new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(7,94,84));
        send.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        send.addActionListener(this);
        send.setForeground(Color.WHITE);
        f.add(send);


        //frame size and location
        f.setSize(450,700);
        f.setLocation(1100,50);
        f.getContentPane().setBackground(Color.white);
        f.setUndecorated(true);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();

            //converting string output to label->panel so as to add it to parameter

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            // new panel for setting message to the right
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);

            //adding right panel to vertical box
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            //adding vertical box to a1 panel
            a1.add(vertical, BorderLayout.PAGE_START);

            //receiving message from server
            dout.writeUTF(out);

            //empty the textbox field
            text.setText("");

            //to have continuous loading of panel once painted
            f.repaint();
            f.invalidate();
            f.validate();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    //function for formatting the text box
    public static  JPanel formatLabel(String out){
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JLabel output=new JLabel("<html><p style=\"width:150px\">"+out+"</p></html>");


        //message box designing
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(37,211,102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50 ));

        panel.add(output);

        //message timings
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        JLabel time=new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }



    public static void main(String[] args) {

        new Client();
        try{
            Socket s=new Socket("127.0.0.1",6001);
            DataInputStream din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());

            while(true){
                String msg=din.readUTF();

                //adding ui to message from client
                JPanel panel=formatLabel(msg);

                //adding this message to the left side using another panel
                JPanel left=new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical,BorderLayout.PAGE_START);
                f.validate();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
