package com.luvsoft.view.component;

import java.io.File;
import java.util.Arrays;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class FileChooser extends Window implements Tree.ExpandListener {

    private VerticalLayout layout;
    private final Panel explorerPanel = new Panel();
    private final Tree tree = new Tree();
    private final File rootDir;
    private String choosenFile = null;
    private Boolean folderOnly = false;
    
    public String getChoosenFile(){
        return this.choosenFile;
    }
    
    public void setChoosenFile(String choosenFile){
        this.choosenFile = choosenFile;
    }
    
    /**
     * 
     * @param instanceRoot
     * @param caption
     * @param folder
     * 
     * folder == true: select only folders
     * folder == false: select only files
     */
    public FileChooser(String instanceRoot, Boolean folderOnly) {
        final Boolean finalFolder = folderOnly;
        this.folderOnly = folderOnly;
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(true);
        this.setDraggable(false);
        this.setWidth("500px");
        this.setHeight("400px");
        this.center();
        this.setCaption("Chọn thư mục lưu file");
        
        layout = new VerticalLayout();
        layout.setSizeFull();
        explorerPanel.setContent(tree);
        explorerPanel.setSizeFull();
        // configure file structure panel
        layout.addComponent(explorerPanel);

        // Ok button
        Button btnSave = new Button("Chọn");
        btnSave.addStyleName(ValoTheme.BUTTON_LARGE);
        btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        // Cancel button
        Button btnCancel = new Button("Hủy");
        btnCancel.addStyleName(ValoTheme.BUTTON_LARGE);
        btnCancel.addStyleName(ValoTheme.BUTTON_PRIMARY);

        HorizontalLayout hzLayout = new HorizontalLayout();
        hzLayout.addComponents(btnSave, btnCancel);
        hzLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        hzLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_CENTER);
        hzLayout.setSpacing(true);

        layout.addComponent(hzLayout);
        layout.setExpandRatio(explorerPanel, 8.0f);
        layout.setExpandRatio(hzLayout, 2.0f);
        layout.setComponentAlignment(hzLayout, Alignment.MIDDLE_CENTER);

        this.setContent(layout);
        // "this" handles tree's expand event
        tree.addExpandListener(this);

        // populate tree's root node with root directory
        rootDir = new File(instanceRoot);
        if (rootDir != null) {
            populateNode(rootDir.getAbsolutePath(), null);
        }
        tree.addItemClickListener( new ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                File tmpFile = new File( event.getItemId().toString() );
                if( !finalFolder ){
                    if( !tmpFile.isDirectory() ){
                        setChoosenFile( event.getItemId().toString() );
                    }
                } else {
                    setChoosenFile( event.getItemId().toString() );
                }
            }
        });

        btnSave.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // TODO Auto-generated method stub
                System.out.println(getChoosenFile());
                close();
            }
        });

        btnCancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                setChoosenFile(null);
                close();
            }
        });
    }

    /**
     * Handle tree expand event, populate expanded node's childs with new files
     * and directories.
     */
    public void nodeExpand(ExpandEvent event) {
        final Item i = tree.getItem(event.getItemId());
        
        if (!tree.hasChildren(i)) {
            // populate tree's node which was expanded
            populateNode(event.getItemId().toString(), event.getItemId());
        }
    }
    

    /**
     * Populates files to tree as items. In this example items are of String
     * type that consist of file path. New items are added to tree and item's
     * parent and children properties are updated.
     *
     * @param file
     *            path which contents are added to tree
     * @param parent
     *            for added nodes, if null then new nodes are added to root node
     */
    private void populateNode(String file, Object parent) {
        final File subdir = new File(file);
        final File[] files = subdir.listFiles();
        if( files != null ){
            Arrays.sort(files);
        }
        for (int x = 0; x < files.length; x++) {
            if( !files[x].getName().toString().startsWith(".") ) {
                try {
                    // add new item (String) to tree
                    final String path = files[x].getCanonicalPath().toString();
                    final String caption = files[x].getName().toString();
                    
                    if( this.folderOnly && !files[x].isDirectory() ){
                        continue;
                    }

                    tree.addItem(path);
                    tree.setItemCaption(path, caption);
                    // set parent if this item has one
                    if (parent != null) {
                        tree.setParent(path, parent);
                    }
                    if( !this.folderOnly ) {
                        // check if item is a directory and read access exists
                        if (files[x].isDirectory() && files[x].canRead()) {
                            // yes, childrens therefore exists
                            tree.setChildrenAllowed(path, true);
                        } else {
                            // no, childrens therefore do not exists
                            tree.setChildrenAllowed(path, false);
                        }
                    } else {
                        // check if item is a directory and read access exists
                        if (files[x].isDirectory() && files[x].canRead()) {
                            // yes, childrens therefore exists
                            tree.setChildrenAllowed(path, true);
                        }
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}