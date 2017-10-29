package co.uk.fernandopinto.aco.view;

import co.uk.fernandopinto.aco.App;
import co.uk.fernandopinto.aco.algo.AntColony;
import co.uk.fernandopinto.aco.algo.Location;
import co.uk.fernandopinto.aco.algo.PathBuilder;
import co.uk.fernandopinto.aco.algo.ProjectModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 27/09/2017.
 */
public class Home extends JFrame {

    private JTextField distanceField;
    private JTextField filePath;
    private JPanel jPanel1;
    private List<float[][]> edges;
    private List<Location> locations = new ArrayList<>();
    private GraphBuilder graphBuilder;
    private App app;
    private View view;
    private SwingWorker<Future<PathBuilder>, Future<PathBuilder>> colonyWorker = null;


    public Home() {

        initComponents();
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        JLabel graphLabel = new JLabel();
        jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        filePath = new JTextField();
        Button browseButton = new Button();
        Button submitButton = new Button();
        JLabel logo = new JLabel();
        JPanel jPanel3 = new JPanel();
        distanceField = new JTextField();
        JLabel jLabel1 = new JLabel();
        JSeparator jSeparator1 = new JSeparator();
        Button runButton = new Button();
        Button resetButton = new Button();
        Button button1 = new java.awt.Button();
        Button button2 = new java.awt.Button();
        Button button3 = new java.awt.Button();
        JLabel jLabel2 = new javax.swing.JLabel();
        Button instructionsButton = new java.awt.Button();
        JLabel jLabel3 = new javax.swing.JLabel();
        Button button4 = new java.awt.Button();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(255, 255, 255));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        jPanel1.setBackground(new Color(255, 255, 255));

        graphLabel.setBackground(new Color(255, 255, 255));
        graphLabel.setIcon(new ImageIcon(getClass().getResource("/v3.png")));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(graphLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(graphLabel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new Color(32, 33, 35));

        filePath.setEditable(false);
        filePath.setForeground(new Color(153, 153, 153));
        filePath.setText("File path");
        filePath.setCaretColor(new Color(204, 204, 204));
        filePath.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        browseButton.setBackground(new Color(126, 87, 194));
        browseButton.setFont(new Font("Tahoma", Font.PLAIN, 12)); // NOI18N
        browseButton.setForeground(new Color(240, 232, 245));
        browseButton.setLabel("Browse");
        browseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browseButtonMouseClicked(evt);
            }
        });

        submitButton.setBackground(new Color(126, 87, 194));
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 12)); // NOI18N
        submitButton.setForeground(new Color(240, 232, 245));
        submitButton.setLabel("Submit");
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    submitButtonMouseClicked(evt);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        logo.setFont(new Font("Blanka", Font.PLAIN, 48)); // NOI18N
        logo.setForeground(new Color(57, 113, 177));
        logo.setText("ACO");

        button1.setBackground(new java.awt.Color(126, 87, 194));
        button1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        button1.setForeground(new java.awt.Color(240, 232, 245));
        button1.setLabel("3");
        button1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button1MouseClicked(evt);
            }
        });

        button2.setBackground(new java.awt.Color(126, 87, 194));
        button2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        button2.setForeground(new java.awt.Color(240, 232, 245));
        button2.setLabel("2");
        button2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button2MouseClicked(evt);
            }
        });

        button3.setBackground(new java.awt.Color(126, 87, 194));
        button3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        button3.setForeground(new java.awt.Color(240, 232, 245));
        button3.setLabel("1");
        button3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button3MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(57, 113, 177));
        jLabel2.setText("Select an example file:");
        jLabel2.setToolTipText("");

        instructionsButton.setBackground(new java.awt.Color(126, 87, 194));
        instructionsButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        instructionsButton.setForeground(new java.awt.Color(240, 232, 245));
        instructionsButton.setLabel("Instructions");
        instructionsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                instructionsButtonMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(57, 113, 177));
        jLabel3.setText("Or choose your own file:");

        button4.setBackground(new java.awt.Color(126, 87, 194));
        button4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        button4.setForeground(new java.awt.Color(240, 232, 245));
        button4.setLabel("4");
        button4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button4MouseClicked(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(instructionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(filePath)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(instructionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                                                                .addComponent(button4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                                                        .addComponent(browseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(filePath, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new Color(32, 33, 35));

        distanceField.setEditable(false);
        distanceField.setBackground(new Color(32, 33, 35));
        distanceField.setFont(new Font("Tahoma", Font.BOLD, 14)); // NOI18N
        distanceField.setForeground(new Color(255, 255, 255));
        distanceField.setHorizontalAlignment(JTextField.CENTER);
        distanceField.setToolTipText("");
        distanceField.setBorder(null);
        distanceField.setCaretColor(new Color(255, 255, 255));

        jLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14)); // NOI18N
        jLabel1.setForeground(new Color(57, 113, 177));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Distance");

        jSeparator1.setBackground(new Color(160, 160, 160));

        runButton.setBackground(new Color(126, 87, 194));
        runButton.setFont(new Font("Tahoma", Font.PLAIN, 12)); // NOI18N
        runButton.setForeground(new Color(240, 232, 245));
        runButton.setLabel("Run");
        runButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                    runButtonMouseClicked(evt);
            }
        });

        resetButton.setBackground(new Color(126, 87, 194));
        resetButton.setFont(new Font("Tahoma", Font.PLAIN, 12)); // NOI18N
        resetButton.setForeground(new Color(240, 232, 245));
        resetButton.setLabel("Reset");
        resetButton.addActionListener(this::resetButtonActionPerformed);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(22, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(distanceField, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(runButton, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(distanceField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(runButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(198, Short.MAX_VALUE))
        );

        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setResizable(false);
        
        add(jPanel2, BorderLayout.NORTH);
        add(jPanel3, BorderLayout.EAST);
        add(jPanel1, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            if(colonyWorker != null) colonyWorker.cancel(true);
        } catch (CancellationException e){
            JOptionPane.showMessageDialog(null, "Job cancelled");
        } finally {
            locations.clear();
            app.clearEdges();
            filePath.setText("File path");
            remove((JPanel) view);
            add(jPanel1, BorderLayout.CENTER);
            distanceField.setText("");
            revalidate();
            repaint();
        }
    }

    private void instructionsButtonMouseClicked(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(null, "To run the algorithm please follow the following 3 steps:\n" +
                "Step 1 - Choose a default file by clicking on one of the buttons 1 to 4, " +
                "or browse your own file.\nStep 2 - Submit the file\nStep 3 - Run\n\n" +
                "Files must be plain text and strictly follow the format given: name of location (up to 10 chars) <space>\n" +
                "int for x coordinate <space> int for y coordinate. Both ints must be between 0 and 9999 and locations\n" +
                "must be placed in sequential rows, with no blank rows before or in between lines or additional info.\n" +
                "Eg.:\nlocationA 346 670\nlocationB 21 450\nlocationC 678 283");
    }

    private void button3MouseClicked(java.awt.event.MouseEvent evt) {
        /*filePath.setForeground(Color.black);
        String path = String.valueOf(getClass().getResource("/example1.txt"));
        if(path.startsWith("file:/")) path = path.replace("file:/", "");*/
        filePath.setText("/example1.txt");
    }

    private void button2MouseClicked(java.awt.event.MouseEvent evt) {
        /*filePath.setForeground(Color.black);
        String path = String.valueOf(getClass().getResource("/example2.txt"));
        if(path.startsWith("file:/")) path = path.replace("file:/", "");*/
        filePath.setText("/example2.txt");
    }

    private void button1MouseClicked(java.awt.event.MouseEvent evt) {
        /*filePath.setForeground(Color.black);
        String path = String.valueOf(getClass().getResource("/example3.txt"));
        if(path.startsWith("file:/")) path = path.replace("file:/", "");*/
        filePath.setText("/example3.txt");
    }

    private void button4MouseClicked(java.awt.event.MouseEvent evt) {
        /*filePath.setForeground(Color.black);
        String path = String.valueOf(getClass().getResource("/example4.txt"));
        if(path.startsWith("file:/")) path = path.replace("file:/", "");*/
        filePath.setText("/example4.txt");
    }

    private void browseButtonMouseClicked(java.awt.event.MouseEvent evt) {

        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            filePath.setForeground(Color.black);
            filePath.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    private void submitButtonMouseClicked(java.awt.event.MouseEvent evt) throws ExecutionException, InterruptedException {

        List<String> lines = null;
        Location location;
        short x;
        short y;
        if(locations.size()>0) {
            JOptionPane.showMessageDialog(null, "Please reset before submitting again");
        } else {
            if (!filePath.getText().equals("File path") && !filePath.getText().isEmpty()) {
                if(filePath.getText().startsWith("C:")) {
                    try {
                        lines = Files.readAllLines(Paths.get(filePath.getText()));
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please try again");
                    }
                } else {
                    try (InputStream resource = Home.class.getResourceAsStream(filePath.getText())) {
                        lines = new BufferedReader(new InputStreamReader(resource,
                                        StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error: Please try again");
                    }
                }
                    for (String line : lines) {
                        String[] arr = line.split(" ");
                        if (arr.length != 3 || arr[0].length() > 10) {
                            JOptionPane.showMessageDialog(null, "Please insert a file within the format specified");
                            locations.clear();
                            return;
                        }
                        try {
                            x = Short.parseShort(arr[1]);
                            y = Short.parseShort(arr[2]);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Please insert a file within the format specified");
                            locations.clear();
                            return;
                        }
                        location = new Location(arr[0], x, y);
                        locations.add(location);
                    }

                filePath.setText("");
                app = new App(locations);
                app.createEdges();
                edges = app.getEdges();
                remove(jPanel1);
                Injector injector = Guice.createInjector(new ProjectModule());
                graphBuilder = injector.getInstance(GraphBuilder.class);
                Viewer viewer = new Viewer(graphBuilder.setupGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
                graphBuilder.setStyleSheet(GraphBuilder.Type.PRIMARY);
                view = viewer.addDefaultView(false);   // false indicates "no JFrame".
                add((JPanel) view, BorderLayout.CENTER);
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Please choose a valid file");
            }
        }
    }

    private void runButtonMouseClicked(java.awt.event.MouseEvent evt) {

        if(locations.size() != 0) {
            graphBuilder.glowNodes();
            graphBuilder.setStyleSheet(GraphBuilder.Type.SECONDARY);
            colonyWorker = new AntColony((short) 300, edges, graphBuilder);
            colonyWorker.addPropertyChangeListener((PropertyChangeEvent event) -> {
                if(event.getPropertyName().equals("state")) {
                    switch ((SwingWorker.StateValue) event.getNewValue()) {
                        case DONE:
                            try {
                                distanceField.setText(String.valueOf((int)colonyWorker.get().get().getTotalDistance()));
                                graphBuilder.setStyleSheet(GraphBuilder.Type.PRIMARY);
                                int[] arr = colonyWorker.get().get().getPaths();
                                for(int j=0; j<arr.length-1; j++){
                                    String index = String.valueOf(arr[j])+String.valueOf(arr[j+1]);
                                    if(arr[j]>arr[j+1]) index = String.valueOf(arr[j+1])+String.valueOf(arr[j]);
                                    graphBuilder.adjustEdges(3, index);
                                }
                                String index = String.valueOf(arr[0])+String.valueOf(arr[arr.length-1]);
                                if(arr[0]>arr[arr.length-1]) index = String.valueOf(arr[arr.length-1])+String.valueOf(arr[0]);
                                graphBuilder.adjustEdges(3, index);
                            } catch (InterruptedException | ExecutionException e) {
                                JOptionPane.showMessageDialog(null, "Error: Please reset and try again");
                            }
                            break;
                        case STARTED:
                        case PENDING:
                            distanceField.setText("Running");
                            break;
                    }
                }
            });
            colonyWorker.execute();
        } else {
            JOptionPane.showMessageDialog(null, "Please submit a valid file");
        }
    }
}