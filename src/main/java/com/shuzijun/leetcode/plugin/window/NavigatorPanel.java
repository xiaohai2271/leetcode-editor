package com.shuzijun.leetcode.plugin.window;


import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.treeStructure.SimpleTree;
import com.shuzijun.leetcode.plugin.listener.QueryKeyListener;
import com.shuzijun.leetcode.plugin.listener.TreeMouseListener;
import com.shuzijun.leetcode.plugin.listener.TreeWillListener;
import com.shuzijun.leetcode.plugin.model.PluginConstant;
import com.shuzijun.leetcode.plugin.model.Question;
import com.shuzijun.leetcode.plugin.renderer.CustomTreeCellRenderer;
import com.shuzijun.leetcode.plugin.utils.DataKeys;
import com.shuzijun.leetcode.plugin.utils.PropertiesUtils;
import icons.LeetCodeEditorIcons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

/**
 * @author shuzijun
 */
public class NavigatorPanel extends SimpleToolWindowPanel implements DataProvider {


    private JPanel queryPanel;
    private JBScrollPane contentScrollPanel;
    private SimpleTree tree;
    private ActionToolbar findToolbar;
    private ActionToolbar actionSortToolbar;

    public NavigatorPanel(ToolWindow toolWindow, Project project) {
        super(Boolean.TRUE, Boolean.TRUE);
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Question("root"));
        tree = new SimpleTree(new DefaultTreeModel(root)) {

            private final JTextPane myPane = new JTextPane();

            {
                myPane.setOpaque(false);
                String addIconText = "'login'";
                String refreshIconText = "'refresh'";
                String configIconText = "'config'";
                String message = PropertiesUtils.getInfo("config.load", addIconText, refreshIconText,configIconText);
                int addIconMarkerIndex = message.indexOf(addIconText);
                myPane.replaceSelection(message.substring(0, addIconMarkerIndex));
                myPane.insertIcon(LeetCodeEditorIcons.LOGIN);
                int refreshIconMarkerIndex = message.indexOf(refreshIconText);
                myPane.replaceSelection(message.substring(addIconMarkerIndex + addIconText.length(), refreshIconMarkerIndex));
                myPane.insertIcon(LeetCodeEditorIcons.REFRESH);
                int configIconMarkerIndex = message.indexOf(configIconText);
                myPane.replaceSelection(message.substring(refreshIconMarkerIndex + refreshIconText.length(), configIconMarkerIndex));
                myPane.insertIcon(LeetCodeEditorIcons.CONFIG);
                myPane.replaceSelection(message.substring(configIconMarkerIndex + configIconText.length()));

            }

            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                }catch (Exception e){
                    return;
                }


                DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
                if (!root.isLeaf()) {
                    return;
                }

                myPane.setFont(getFont());
                myPane.setBackground(getBackground());
                myPane.setForeground(getForeground());
                Rectangle bounds = getBounds();
                myPane.setBounds(0, 0, bounds.width - 10, bounds.height);

                Graphics g2 = g.create(bounds.x + 10, bounds.y + 20, bounds.width, bounds.height);
                try {
                    myPane.paint(g2);
                } finally {
                    g2.dispose();
                }
            }
        };
        tree.getEmptyText().clear();
        //tree.setRowHeight(21);
        tree.setOpaque(false);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.addMouseListener(new TreeMouseListener(tree, project));
        tree.addTreeWillExpandListener(new TreeWillListener(tree, toolWindow, project));


        ActionToolbar actionToolbar = actionManager.createActionToolbar(PluginConstant.ACTION_PREFIX + " Toolbar",
                (DefaultActionGroup) actionManager.getAction(PluginConstant.LEETCODE_NAVIGATOR_ACTIONS_TOOLBAR),
                true);

        actionToolbar.setTargetComponent(tree);
        setToolbar(actionToolbar.getComponent());

        SimpleToolWindowPanel treePanel = new SimpleToolWindowPanel(Boolean.TRUE, Boolean.TRUE);

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        contentScrollPanel = new JBScrollPane(tree, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupPanel.add(contentScrollPanel);

        treePanel.setContent(groupPanel);

        queryPanel = new JPanel();
        queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));
        JTextField queryField = new JBTextField();
        queryField.setToolTipText("Enter Search");
        queryField.addKeyListener(new QueryKeyListener(queryField, contentScrollPanel, toolWindow));
        queryPanel.add(queryField);

         findToolbar = actionManager.createActionToolbar("",
                (DefaultActionGroup) actionManager.getAction(PluginConstant.LEETCODE_FIND_TOOLBAR),
                true);
        findToolbar.setTargetComponent(tree);
        actionSortToolbar = actionManager.createActionToolbar("",
                (DefaultActionGroup) actionManager.getAction(PluginConstant.LEETCODE_FIND_SORT_TOOLBAR),
                true);
        actionSortToolbar.setTargetComponent(tree);
        queryPanel.add(findToolbar.getComponent());
        queryPanel.add(actionSortToolbar.getComponent());

        queryPanel.setVisible(false);
        treePanel.setToolbar(queryPanel);
        setContent(treePanel);

    }

    @Override
    public Object getData(String dataId) {
        if (DataKeys.LEETCODE_PROJECTS_TREE.is(dataId)) {
            return tree;
        }

        if (DataKeys.LEETCODE_PROJECTS_TERRFIND.is(dataId)) {
            return queryPanel;
        }

        if (DataKeys.LEETCODE_PROJECTS_SCROLL.is(dataId)) {
            return contentScrollPanel;
        }
        if (DataKeys.LEETCODE_TOOLBAR_FIND.is(dataId)) {
            return findToolbar;
        }
        if (DataKeys.LEETCODE_TOOLBAR_SORT.is(dataId)) {
            return actionSortToolbar;
        }
        return super.getData(dataId);
    }
}
