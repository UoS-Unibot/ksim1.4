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

/**
 * Contains controls for the visualiser.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class VisualiserControls extends JPanel {

    private final JButton btnRun = new JButton("Run");
    private final JButton btnPause = new JButton("Pause");
    private final JTextField tfX = new JTextField();
    private final JTextField tfY = new JTextField();
    private final JTextField tfH = new JTextField();
    private VisualiserListener listener;
    private DecimalFormat strFormat = new DecimalFormat( "#0.0");

    public VisualiserControls() {
        super();
        setPreferredSize(new Dimension(800, 70));
        setBorder(BorderFactory.createEtchedBorder());

        btnPause.setEnabled(false);
        btnRun.setEnabled(true);
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run();
            }

        });

        btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause();
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
        add(btnPause, gbc);

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
        if (listener != null) {
            listener.setRunning(true);
        }
        btnRun.setEnabled(false);
        btnPause.setEnabled(true);
    }

    void pause() {
        if (listener != null) {
            listener.setRunning(false);
        }
        btnPause.setEnabled(false);
        btnRun.setEnabled(true);
    }

    void restart() {
        if (listener != null) {
            listener.restart();
        }
        btnPause.setEnabled(false);
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
