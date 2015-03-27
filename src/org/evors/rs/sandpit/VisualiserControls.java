package org.evors.rs.sandpit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Contains controls for the visualiser.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class VisualiserControls extends JPanel {

    private final JButton btnRun = new JButton("Run");
    private final JButton btnPause = new JButton("Pause");
    private VisualiserListener listener;

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

}
