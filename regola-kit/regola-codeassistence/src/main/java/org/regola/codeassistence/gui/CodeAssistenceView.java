/*
 * CodeAssistenceView.java
 */

package org.regola.codeassistence.gui;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.FullStack;
import org.regola.codeassistence.Options;
import org.regola.codeassistence.generator.Generator;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * The application's main frame.
 */
public class CodeAssistenceView extends FrameView {

	public CodeAssistenceView(SingleFrameApplication app) {
        super(app);

        jsyntaxpane.DefaultSyntaxKit.initKit();

        initComponents();
        
        if (CodeAssistenceApp.getModelClass()!= null)
        {
        	String name  = CodeAssistenceApp.getModelClass();
    		name = name.replaceAll("\\.", "/");
	       jModelTree.selectModelClass(new File( "./src/main/java/" + name + ".java"  ));
        }
        
        
        
        jModelTree.getTree().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				jModelTreeValueChanged(e);
				
			}
        	
        });
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    protected void jModelTreeValueChanged(TreeSelectionEvent e) {
		preview();
	}

	@Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = CodeAssistenceApp.getApplication().getMainFrame();
            aboutBox = new CodeAssistenceAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CodeAssistenceApp.getApplication().show(aboutBox);
    }
	
	public void showAlert(String message) {
		
		 JFrame mainFrame = CodeAssistenceApp.getApplication().getMainFrame();
		 JOptionPane.showMessageDialog(mainFrame, message);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new javax.swing.JPanel();
        javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jGeneratosDAO = new javax.swing.JList();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jFilesDAO = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorDAO = new javax.swing.JEditorPane();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jGeneratosService = new javax.swing.JList();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jEditorService = new javax.swing.JEditorPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jFilesService = new javax.swing.JList();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jGeneratorsPresentation = new javax.swing.JList();
        javax.swing.JLabel jLabel12 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel13 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel14 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jEditorPresentation = new javax.swing.JEditorPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        jFilesPresentation = new javax.swing.JList();
        javax.swing.JLabel jLabel15 = new javax.swing.JLabel();
        jModelTree = new org.regola.codeassistence.gui.ModelPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jSplitPane1.setDividerLocation(90);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.regola.codeassistence.gui.CodeAssistenceApp.class).getContext().getResourceMap(CodeAssistenceView.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jGeneratosDAO.setName("jGeneratosDAO"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${allDAOGeneratos}");
        org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jGeneratosDAO);
        bindingGroup.addBinding(jListBinding);

        jGeneratosDAO.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jGeneratosDAOValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jGeneratosDAO);

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel3.setName("jLabel3"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jGeneratosDAO, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.description}"), jLabel3, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jFilesDAO.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Choose a generator" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jFilesDAO.setName("jFilesDAO"); // NOI18N
        jFilesDAO.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jFilesDAOValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jFilesDAO);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jEditorDAO.setBackground(resourceMap.getColor("jEditorDAO.background")); // NOI18N
        jEditorDAO.setName("jEditorDAO"); // NOI18N
        jScrollPane3.setViewportView(jEditorDAO);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("DAO", jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jGeneratosService.setName("jGeneratosService"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${allServicesGeneratos}");
        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jGeneratosService);
        bindingGroup.addBinding(jListBinding);

        jGeneratosService.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jGeneratosServiceValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jGeneratosService);

        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel7.setName("jLabel7"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jGeneratosService, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.description}"), jLabel7, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jEditorService.setBackground(resourceMap.getColor("jEditorService.background")); // NOI18N
        jEditorService.setName("jEditorService"); // NOI18N
        jScrollPane5.setViewportView(jEditorService);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jFilesService.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Choose a generator" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jFilesService.setName("jFilesService"); // NOI18N
        jFilesService.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jFilesServiceValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jFilesService);

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jGeneratorsPresentation.setName("jGeneratorsPresentation"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${allPresentationGeneratos}");
        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jGeneratorsPresentation);
        bindingGroup.addBinding(jListBinding);

        jGeneratorsPresentation.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jGeneratorsPresentationValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(jGeneratorsPresentation);

        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel12.setName("jLabel12"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jGeneratorsPresentation, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.description}"), jLabel12, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        jEditorPresentation.setBackground(resourceMap.getColor("jEditorPresentation.background")); // NOI18N
        jEditorPresentation.setName("jEditorPresentation"); // NOI18N
        jScrollPane8.setViewportView(jEditorPresentation);

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        jFilesPresentation.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Choose a generator" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jFilesPresentation.setName("jFilesPresentation"); // NOI18N
        jFilesPresentation.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jFilesPresentationValueChanged(evt);
            }
        });
        jScrollPane9.setViewportView(jFilesPresentation);

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane9)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jSplitPane1.setRightComponent(jTabbedPane1);

        jModelTree.setName("jModelTree"); // NOI18N
        jSplitPane1.setLeftComponent(jModelTree);

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.regola.codeassistence.gui.CodeAssistenceApp.class).getContext().getActionMap(CodeAssistenceView.class, this);
        jButton1.setAction(actionMap.get("generate")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setAction(actionMap.get("masterDetails")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAction(actionMap.get("generate")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 418, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    
    public boolean isReadyToGenerate()
    {
    	return getSelectedModelName() != null && getSelectedGenerators().size()>0;
    }
   

    @SuppressWarnings("unchecked")
	private List<Generator> getSelectedGenerators() {
		List generators = new ArrayList<Generator>();
 
		generators.addAll(Arrays.asList(jGeneratosDAO.getSelectedValues()));
		generators.addAll(Arrays.asList(jGeneratosService.getSelectedValues()));
		generators.addAll(Arrays.asList(jGeneratorsPresentation.getSelectedValues()));
		
		return generators;
	}
    
    @SuppressWarnings("unchecked")
	private List<Generator> getSelectedDAOGenerators() {
    	List generators = Arrays.asList(jGeneratosDAO.getSelectedValues());
    	return generators;
    }
    
    @SuppressWarnings("unchecked")
	private List<Generator> getSelectedPresentationGenerators() {
    	List generators = Arrays.asList(jGeneratorsPresentation.getSelectedValues());
    	return generators;
    }
    
    @SuppressWarnings("unchecked")
	private List<Generator> getSelectedServiceGenerators() {
    	List generators = Arrays.asList(jGeneratosService.getSelectedValues());
    	return generators;
    }

	private void jGeneratosDAOValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jGeneratosDAOValueChanged
    	
    	//Generator generator = (Generator) ((JList) evt.getSource()).getSelectedValue(); ;
    	preview();
    	
    }//GEN-LAST:event_jGeneratosDAOValueChanged

	private void preview()
	{
		if (!isReadyToGenerate()) return;
		
		String modelName = getSelectedModelName();
		filesDAO.clear();
		filesPresentation.clear();
		filesService.clear();
		Environment env = new Environment();
		
		try 
		{
			for (Generator generator : getSelectedDAOGenerators())
			{
				filesDAO.putAll( generator.simulate(env, FullStack.instanceParameterBuilder(env, modelName)));
			}
			
			for (Generator generator : getSelectedServiceGenerators())
			{
				filesService.putAll( generator.simulate(env, FullStack.instanceParameterBuilder(env, modelName)));
			}
			
			for (Generator generator : getSelectedPresentationGenerators())
			{
				filesPresentation.putAll( generator.simulate(env, FullStack.instanceParameterBuilder(env, modelName)));
			}
		} catch (Exception e)
		{
			showAlert(e.getMessage());
			return;
		}
		
		jFilesDAO.setModel(new javax.swing.AbstractListModel() {
			Object[] values =  filesDAO.keySet().toArray();
			public int getSize() { return values.length; }
			public Object getElementAt(int i) { return values[i]; }
		});
		
		jFilesService.setModel(new javax.swing.AbstractListModel() {
			Object[] values =  filesService.keySet().toArray();
			public int getSize() { return values.length; }
			public Object getElementAt(int i) { return values[i]; }
		});
		
		jFilesPresentation.setModel(new javax.swing.AbstractListModel() {
			Object[] values =  filesPresentation.keySet().toArray();
			public int getSize() { return values.length; }
			public Object getElementAt(int i) { return values[i]; }
		});
	}
	

	
	private void jFilesDAOValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jFilesDAOValueChanged
		String fileName = (String) ((JList) evt.getSource()).getSelectedValue();
		jEditorDAO.setContentType(fileName.endsWith(".xml") ? "text/xml" : "text/java");
		jEditorDAO.setText(filesDAO.get(fileName));
		
	}//GEN-LAST:event_jFilesDAOValueChanged

	private void jGeneratosServiceValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jGeneratosServiceValueChanged
		preview();
	}//GEN-LAST:event_jGeneratosServiceValueChanged
	
	private void jFilesServiceValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jFilesServiceValueChanged
		String fileName = (String) ((JList) evt.getSource()).getSelectedValue();
		jEditorService.setContentType(fileName.endsWith(".xml") ? "text/xml" : "text/java");
		jEditorService.setText(filesService.get(fileName));

	}//GEN-LAST:event_jFilesServiceValueChanged
	
	private void jGeneratorsPresentationValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jGeneratorsPresentationValueChanged
		preview();
	}//GEN-LAST:event_jGeneratorsPresentationValueChanged

	private void jFilesPresentationValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jFilesPresentationValueChanged
		String fileName = (String) ((JList) evt.getSource()).getSelectedValue();
		jEditorPresentation.setContentType(fileName.endsWith(".xml") ? "text/xml" : "text/java");
		jEditorPresentation.setText(filesPresentation.get(fileName));

	}//GEN-LAST:event_jFilesPresentationValueChanged
	
    private String getSelectedModelName() {
		return jModelTree.getLastSelectedModelClass();
	}

	protected Map<String,String> filesDAO = new HashMap<String, String>();
	protected Map<String,String> filesService = new HashMap<String, String>();
	protected Map<String,String> filesPresentation = new HashMap<String, String>();

	
	public List<Generator> getAllDAOGeneratos() {
		return Arrays.asList(Options.getDAOGenerators());
	}
	
	public List<Generator> getAllServicesGeneratos() {
		return Arrays.asList(Options.getServiceGenerators());
	}
	
	public List<Generator> getAllPresentationGeneratos() {
		return Arrays.asList(Options.getPresentationGenerators());
	}

    @Action
    public Task Preview() {
        return new PreviewTask(getApplication());
    }

    private class PreviewTask extends org.jdesktop.application.Task<Object, Void> {
        PreviewTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to PreviewTask fields, here.
            super(app);
        }
        @Override protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    @Action
    public Task generate() {
        return new GenerateTask(getApplication());
    }

    private class GenerateTask extends org.jdesktop.application.Task<Object, Void> {
        GenerateTask(org.jdesktop.application.Application app) {
           
            super(app);
        }
        
        @Override protected Object doInBackground() {
           
        	if (!isReadyToGenerate()) return null;
    		
    		String modelName = getSelectedModelName();
    		
    		Environment env = new Environment();
    		
    		try 
    		{
    			for (Generator generator : getSelectedGenerators())
    			{
    				generator.generate(env, FullStack.instanceParameterBuilder(env, modelName));
    			}
    			
    		} catch (Exception e)
    		{
    			showAlert(e.getMessage());
    			return null;
    		}
    		
        	return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
           
        }
    }

    @Action
    public void masterDetails() {
    
    	jGeneratosDAO.setSelectionInterval(0, jGeneratosDAO.getModel().getSize()-1);
    	jGeneratosService.setSelectionInterval(0, jGeneratosService.getModel().getSize()-1);
    	jGeneratorsPresentation.setSelectionInterval(0, jGeneratorsPresentation.getModel().getSize()-1);
    	
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JEditorPane jEditorDAO;
    private javax.swing.JEditorPane jEditorPresentation;
    private javax.swing.JEditorPane jEditorService;
    private javax.swing.JList jFilesDAO;
    private javax.swing.JList jFilesPresentation;
    private javax.swing.JList jFilesService;
    private javax.swing.JList jGeneratorsPresentation;
    private javax.swing.JList jGeneratosDAO;
    private javax.swing.JList jGeneratosService;
    private javax.swing.JMenuItem jMenuItem1;
    private org.regola.codeassistence.gui.ModelPanel jModelTree;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
