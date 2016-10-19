package org.evors.rs.sandpit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.evors.core.geometry.Vec2;
import org.evors.vision.HaarFilter;

/**
 * Contains controls for the visualiser.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class VisualiserControls extends JPanel {

    private final JButton btnRun = new JButton("Run");
    private final JButton btnOverlay = new JButton("Sensor Overlay");
    private final JTextField tfX = new JTextField();
    private final JTextField tfY = new JTextField();
    private final JTextField tfH = new JTextField();
    private VisualiserListener listener;
    private DecimalFormat strFormat = new DecimalFormat( "#0.0");

    public VisualiserControls() {
        super();
        setPreferredSize(new Dimension(800, 70));
        setBorder(BorderFactory.createEtchedBorder());

        btnOverlay.setEnabled(true);
        btnRun.setEnabled(true);
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run();
            }

        });

        btnOverlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                overlayonoff();
            }
        });

        tfX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setX();
            }
        });
        
        tfY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setY();
            }
        });
        
        tfH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setHeading();
            }
        });
        
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(btnRun, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(btnOverlay, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add( tfX, gbc );
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add( tfY, gbc );
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add( tfH, gbc );
    }

    public VisualiserListener getListener() {
        return listener;
    }

    public void setListener(VisualiserListener listener) {
        this.listener = listener;
    }

    void run() {
    	if( btnRun.getText().equals("Run") )
    	{
	        if (listener != null) {
	            listener.setRunning(true);
	            btnRun.setText( "Pause" );
	        }
    	}else
    	{
    		 if (listener != null) {
    	            listener.setRunning(false);
    	        }
    		 btnRun.setText("Run");
    	}
        
//        btnRun.setEnabled(false);
  //      btnPause.setEnabled(true);
    }

    void overlayonoff() {
    	HaarFilter.overlayFilterOnDebugImage = !HaarFilter.overlayFilterOnDebugImage;
    }

    void restart() {
        if (listener != null) {
            listener.restart();
        }
        btnOverlay.setEnabled(false);
        btnRun.setEnabled(true);
    }
    
    void setX() {
        if (listener != null) {
            listener.setX( Double.parseDouble( tfX.getText() ));
        }
    }

    void setY() {
        if (listener != null) {
        	listener.setY( Double.parseDouble( tfY.getText() ));
        }
    }

    void setHeading() {
        if (listener != null) {
        	listener.setHeading( Double.parseDouble( tfH.getText() ));
        }
    }


}
