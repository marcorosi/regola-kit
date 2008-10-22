package org.regola.codeassistence.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.io.DirectoryWalker;

/**
 * Based on the class FileTreePanel by Kirill Grouchnikov
 * 
 * @author Nicola Santi
 */

public class ModelPanel extends JPanel {
	/**
	 * File system view.
	 */
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();

	/**
	 * Renderer for the file tree.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
		/**
		 * Icon cache to speed the rendering.
		 */
		private Map<String, Icon> iconCache = new HashMap<String, Icon>();

		/**
		 * Root name cache to speed the rendering.
		 */
		private Map<File, String> rootNameCache = new HashMap<File, String>();

		public static String decorateName(File packageDir) {

			String name = "-";
			name = packageDir.getPath();
			name = name.replaceAll("/", ".");
			name = name.replaceAll("\\.\\.src\\.main\\.java\\.", "");

			return name;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
		 * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
		 * boolean)
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			FileTreeNode ftn = (FileTreeNode) value;
			File file = ftn.file;
			String filename = "";
			if (file != null) {
				if (ftn.isFileSystemRoot) {
					// long start = System.currentTimeMillis();
					filename = this.rootNameCache.get(file);
					if (filename == null) {
						// filename = fsv.getSystemDisplayName(file);
						filename = decorateName(file);
						this.rootNameCache.put(file, filename);
					}
					// long end = System.currentTimeMillis();
					// System.out.println(filename + ":" + (end - start));
				} else {
					filename = file.getName();
					//filename = decorateName(file);
				}
			}
			JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
					filename, sel, expanded, leaf, row, hasFocus);
			if (file != null) {
				Icon icon = this.iconCache.get(filename);
				if (icon == null) {
					// System.out.println("Getting icon of " + filename);
					icon = fsv.getSystemIcon(file);
					this.iconCache.put(filename, icon);
				}
				result.setIcon(icon);
			}
			return result;
		}
	}

	/**
	 * A node in the file tree.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class FileTreeNode implements TreeNode {
		/**
		 * Node file.
		 */
		private File file;

		/**
		 * Children of the node file.
		 */
		private File[] children;

		/**
		 * Parent node.
		 */
		private TreeNode parent;

		/**
		 * Indication whether this node corresponds to a file system root.
		 */
		private boolean isFileSystemRoot;

		/**
		 * Creates a new file tree node.
		 * 
		 * @param file
		 *            Node file
		 * @param isFileSystemRoot
		 *            Indicates whether the file is a file system root.
		 * @param parent
		 *            Parent node.
		 */
		public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
			this.file = file;
			this.isFileSystemRoot = isFileSystemRoot;
			this.parent = parent;
			 
			this.children = this.file.listFiles(new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					
					return !pathname.isDirectory();
				}
				
			});
			if (this.children == null)
				this.children = new File[0];
		}

		/**
		 * Creates a new file tree node.
		 * 
		 * @param children
		 *            Children files.
		 */
		public FileTreeNode(File[] children) {
			this.file = null;
			this.parent = null;
			this.children = children;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#children()
		 */
		public Enumeration<?> children() {
			final int elementCount = this.children.length;
			return new Enumeration<File>() {
				int count = 0;

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.Enumeration#hasMoreElements()
				 */
				public boolean hasMoreElements() {
					return this.count < elementCount;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.Enumeration#nextElement()
				 */
				public File nextElement() {
					if (this.count < elementCount) {
						return FileTreeNode.this.children[this.count++];
					}
					throw new NoSuchElementException("Vector Enumeration");
				}
			};

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#getAllowsChildren()
		 */
		public boolean getAllowsChildren() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#getChildAt(int)
		 */
		public TreeNode getChildAt(int childIndex) {
			return new FileTreeNode(this.children[childIndex],
					this.parent == null, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#getChildCount()
		 */
		public int getChildCount() {
			return this.children.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
		 */
		public int getIndex(TreeNode node) {
			FileTreeNode ftn = (FileTreeNode) node;
			for (int i = 0; i < this.children.length; i++) {
				if (ftn.file.equals(this.children[i]))
					return i;
			}
			return -1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#getParent()
		 */
		public TreeNode getParent() {
			return this.parent;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeNode#isLeaf()
		 */
		public boolean isLeaf() {
			return (this.getChildCount() == 0);
		}

		public File getFile() {
			// TODO Auto-generated method stub
			return file;
		}
	}

	/**
	 * The file tree.
	 */
	private JTree tree;

	public JTree getTree() {
		return tree;
	}

	/**
	 * Creates the file tree panel.
	 */
	public ModelPanel() {
		this.setLayout(new BorderLayout());

		File[] roots = new ModelPackagesFinder().find(new File(
				"./src/main/java"));
		
		FileTreeNode rootTreeNode = new FileTreeNode(roots);
		this.tree = new JTree(rootTreeNode);
		this.tree.setCellRenderer(new FileTreeCellRenderer());
		this.tree.setRootVisible(false);
		final JScrollPane jsp = new JScrollPane(this.tree);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.add(new JLabel("Model's objects:"), BorderLayout.NORTH);
		this.add(jsp, BorderLayout.CENTER);
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		// Traverse tree from root
		expandAll(tree, new TreePath(root), true);
	}
	
	public String getLastSelectedModelClass()
	{
		FileTreeNode node = (FileTreeNode) getTree().getLastSelectedPathComponent();
		
		if (node == null) return null;
		
		String name = FileTreeCellRenderer.decorateName(node.getFile());
		
		if (!name.endsWith(".java")) return null;
		
		return name.replace(".java", "");
	}
	

	public class ModelPackagesFinder extends DirectoryWalker {

		public ModelPackagesFinder() {
			super();
		}

		public File[] find(File startDirectory) {
			List<File> results = new ArrayList<File>();
			try {
				walk(startDirectory, results);
			} catch (IOException e) {
				throw new RuntimeException(e);

			}
			
			File[] files = {};
			return results.toArray(files);
		}

		protected boolean handleDirectory(File directory, int depth,
				Collection results) {
			return true;
		}

		protected void handleFile(File file, int depth, Collection results) {

			String parent = file.getParent();
			if (parent.contains("model") && file.getName().endsWith(".java")) {
				File parentDir = new File(parent);
				if (!results.contains(parentDir))
					results.add(parentDir);
			}

		}
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (int i=0; i<node.getChildCount(); ++i)  {
				
				TreeNode n = node.getChildAt(i);
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}
	
	public void selectModelClass(File model)
	{
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		selectModel(tree, new TreePath(root), model);
	}
	
	private void selectModel(JTree tree, TreePath parent, File model) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		FileTreeNode fileNode = (FileTreeNode) node;
		
		if (fileNode.getFile()!=null && fileNode.getFile().equals(model))
		{
			tree.setSelectionPath(parent);
		}
		
		
		if (node.getChildCount() >= 0) {
			for (int i=0; i<node.getChildCount(); ++i)  {
				
				TreeNode n = node.getChildAt(i);
				TreePath path = parent.pathByAddingChild(n);
				selectModel(tree, path, model);
			}
		}


	}


}
