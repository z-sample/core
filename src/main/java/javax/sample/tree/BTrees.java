package javax.sample.tree;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zero
 *         Created on 2016/11/23.
 */
public class BTrees {

    /**
     * <p> 判断是否为平衡二叉树 </p>
     * <p>
     * 平衡二叉树（Balanced Binary Tree）又被称为AVL树（有别于AVL算法）。
     * 它是一 棵空树或它的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。平衡二叉树的常用实现方法有红黑树、AVL、替罪羊树、Treap、伸展树等。
     * </p>
     *
     * @param node   当前树
     * @param height 当前树的高度(方法执行完后会计算出当前树的高度)
     * @return
     */

    public static boolean isAVLTree(Node node, AtomicInteger height) {
        if (node == null) {//递归结束
            height.set(0);
            return true;
        }
        AtomicInteger leftHeight = new AtomicInteger();
        AtomicInteger rightHeight = new AtomicInteger();
        boolean bl = isAVLTree(node.subLeft, leftHeight);
        boolean br = isAVLTree(node.subRight, rightHeight);
        //父节点的高度=max(左子树高度,右子树高度)+1
        height.set(Math.max(leftHeight.get(), rightHeight.get()) + 1);
//        System.out.println(leftHeight.get() + " - " + rightHeight.get());
        // 左子树和右子树都是AVL，并且高度相差不大于1，返回真
        if (bl && br && Math.abs(leftHeight.get() - rightHeight.get()) <= 1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 求二叉树中的节点个数
     *
     * @param tree
     * @return
     */
    public static int nodeCount(Node tree) {
        //1.如果二叉树为空,返回0
        if (tree == null) {//递归结束
            return 0;
        }
        //2.如果二叉树不为空，二叉树节点个数 = 左子树节点个数 + 右子树节点个数 + 1
        //不要忘了把自己加上(+1)
        return nodeCount(tree.subLeft) + nodeCount(tree.subRight) + 1;
    }

    /**
     * 求二叉树的深度
     *
     * @param tree
     * @return
     */
    public static int treeDepth(Node tree) {
        //1.如果二叉树为空,返回0
        if (tree == null) {//递归结束
            return 0;
        }
        //2.如果二叉树不为空，返回最深的子树深度值再加上一
        //不要忘了把自己加上(+1)
        return Math.max(treeDepth(tree.subLeft), treeDepth(tree.subRight)) + 1;
    }

    /**
     * <pre>
     * 解题思路:
     * 1.如果二叉树为空或者layer<1返回0
     * 2.如果二叉树不为空并且layer==1，返回1
     * 3.如果二叉树不为空且layer>1，返回左子树中layer-1层的节点个数与右子树layer-1层节点个数之和
     * </pre>
     * 关键是第三步, 这是一个典型的将大任务分成n个小任务来处理案例。
     *
     * @param tree  已存在的树
     * @param layer 待计算节点数的层
     * @return 返回该层的总节点数
     */
    public static int nodeCount(Node tree, int layer) {
        //1.如果二叉树为空或者layer<1返回0
        if (tree == null || layer < 1) {//递归结束
            return 0;
        }
        //2.如果二叉树不为空并且layer==1，返回1
        if (layer == 1) return 1;
        //3.如果二叉树不为空且layer>1，返回左子树中layer-1层的节点个数与右子树layer-1层节点个数之和
        //tree.subLeft的layer-1层就是tree的layer层中的左半边
        return nodeCount(tree.subLeft, layer - 1) + nodeCount(tree.subRight, layer - 1);
    }

    public static void main(String[] args) {
        Node root = new Node(0);//第一层
        root.subLeft = new Node(11);//第二层L
        root.subRight = new Node(12);//第二层R
        root.subLeft.subLeft = new Node(21);//第三层
        root.subLeft.subLeft.subLeft = new Node(31);//第四层
        AtomicInteger height = new AtomicInteger();
        System.out.println("Is AVL Tree:" + isAVLTree(root, height));
        System.out.println("Tree Height:" + height);
        System.out.println("------------------------");
        System.out.println(nodeCount(root, 4));
        System.out.println("------------------------");
        System.out.println(nodeCount(root));
        System.out.println("------------------------treeDepth");
        System.out.println(treeDepth(root));
    }

}
