package javax.sample.tree;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Zero
 *         Created on 2016/11/23.
 */
public class BBTrees {

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
        //父结点的高度=max(左子树高度,右子树高度)+1
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
     * 求二叉树中的结点个数
     *
     * @param tree
     * @return
     */
    public static int nodeCount(Node tree) {
        //1.如果二叉树为空,返回0
        if (tree == null) {//递归结束
            return 0;
        }
        //2.如果二叉树不为空，二叉树结点个数 = 左子树结点个数 + 右子树结点个数 + 1
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
     * 3.如果二叉树不为空且layer>1，返回左子树中layer-1层的结点个数与右子树layer-1层结点个数之和
     * </pre>
     * 关键是第三步, 这是一个典型的将大任务分成n个小任务来处理案例。
     *
     * @param tree  已存在的树
     * @param layer 待计算结点数的层
     * @return 返回该层的总结点数
     */
    public static int nodeCount(Node tree, int layer) {
        //1.如果二叉树为空或者layer<1返回0
        if (tree == null || layer < 1) {//递归结束
            return 0;
        }
        //2.如果二叉树不为空并且layer==1，返回1
        if (layer == 1) return 1;
        //3.如果二叉树不为空且layer>1，返回左子树中layer-1层的结点个数与右子树layer-1层结点个数之和
        //tree.subLeft的layer-1层就是tree的layer层中的左半边
        return nodeCount(tree.subLeft, layer - 1) + nodeCount(tree.subRight, layer - 1);
    }

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 前序遍历(先根遍历): 根结点->左子树->右子树
     *
     * @param tree
     */
    public static void preOrderTraverse(Node tree) {
        if (tree == null) {
            return;
        }
        System.out.format(" => 访问:%d", tree.value);//访问跟结点
        preOrderTraverse(tree.subLeft);
        preOrderTraverse(tree.subRight);
    }

    /**
     * 后序遍历(后根遍历): 左子树->右子树->根结点
     *
     * @param tree
     */
    public static void postOrderTraverse(Node tree) {
        if (tree == null) {
            return;
        }
        postOrderTraverse(tree.subLeft);
        postOrderTraverse(tree.subRight);
        System.out.println(tree.value);//访问跟结点
    }

    /**
     * 中序遍历(中根遍历): 左子树->根结点->右子树
     *
     * @param tree
     */
    public static void inOrderTraverse(Node tree) {
        if (tree == null) {
            return;
        }
        inOrderTraverse(tree.subLeft);
        System.out.println(tree.value);//访问跟结点
        inOrderTraverse(tree.subRight);
    }

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 分层遍历:上下左右
     *
     * @param tree
     */
    public static void levelOrderTraverse(Node tree) {
        if (tree == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<Node>();
        Node current;
        queue.offer(tree);//将根结点入队
        while (!queue.isEmpty()) {
            current = queue.poll();//出队队头元素并访问
            System.out.format(" => 访问:%d", current.value);
            //如果当前结点的左结点不为空入队
            if (current.subLeft != null) {
                queue.offer(current.subLeft);
            }
            //如果当前结点的右结点不为空，把右结点入队
            if (current.subRight != null) {
                queue.offer(current.subRight);
            }
        }

    }

    /**
     * 求二叉树的镜像:全部左右结点互换位置, 就像照镜子一样
     *
     * @param tree
     */
    public static void mirror(Node tree) {
        if (tree == null) {
            return;
        }
        Node temp = tree.subLeft;
        tree.subLeft = tree.subRight;
        tree.subRight = temp;
        mirror(tree.subLeft);
        mirror(tree.subRight);
    }

    //求二叉树中结点的最大距离
    //max(Smax左,Smax右,左深度+右深度-1)
    //当右子树为空时,最大距离就是左子树的最大距离
    //当左子树为空时,最大距离就是右子树的最大距离


    public static void main(String[] args) {
        /**
         *
         */
        //
        Node root = new Node(0);//第一层
        root.subLeft = new Node(11);//第二层L
        root.subRight = new Node(12);//第二层R
        root.subLeft.subLeft = new Node(21);//第三层
        root.subLeft.subLeft.subLeft = new Node(31);//第四层
        root.subLeft.subLeft.subRight = new Node(32);
        root.subLeft.subLeft.subLeft.subRight = new Node(42);
        AtomicInteger height = new AtomicInteger();
        System.out.println("Is AVL Tree:" + isAVLTree(root, height));
        System.out.println("Tree Height:" + height);
        System.out.println("------------------------");
        System.out.println(nodeCount(root, 4));
        System.out.println("------------------------");
        System.out.println(nodeCount(root));
        System.out.println("------------------------树深度");
        System.out.println(treeDepth(root));
        System.out.println("------------------------前序遍历");
        preOrderTraverse(root);
        System.out.println("\n------------------------层次遍历");
        levelOrderTraverse(root);
        System.out.println("\n------------------------mirror");
        mirror(root);
        preOrderTraverse(root);
        System.out.println("\n------------------------mirror");
    }



}
