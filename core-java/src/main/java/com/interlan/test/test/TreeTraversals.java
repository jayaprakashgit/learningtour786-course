package com.interlan.test.test;

import java.util.Deque;
import java.util.LinkedList;

public class TreeTraversals {

    public void inOrder(Node root){
        if(root == null){
            return;
        }
        inOrder(root.left);
        System.out.print(root.data + " ");
        inOrder(root.right);
    }
    
    public void preOrder(Node root){
        if(root == null){
            return;
        }
        System.out.print(root.data + " ");
        preOrder(root.left);
        preOrder(root.right);
    }
    
    public void postOrder(Node root){
        if(root == null){
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.data + " ");
    }

    public void inorderItr(Node root){
        Deque<Node> stack = new LinkedList<Node>();
        Node node = root;
        while(true){
            if(node != null){
                stack.addFirst(node);
                node = node.left;
            }
            else{
                if(stack.isEmpty()){
                    break;
                }
                node = stack.pollFirst();
                System.out.println(node.data);
                node = node.right;
            }
        }
    }
    
    public void morrisTraversalInOrder(Node root){
        while(root != null){
            if(root.left == null){
                System.out.println(root.data);
                root = root.right;
            }
            else{
                Node pre = root.left;
                while(pre.right != root && pre.right != null){
                    pre = pre.right;
                }
                if(pre.right == root){
                    pre.right = null;
                    System.out.println(root.data);
                    root = root.right;
                }else{
                    pre.right = root;
                    root = root.left;
                }
            }
        }
    }

    public void morrisTraversalPreOrder(Node root){
        while(root != null){
            if(root.left == null){
                System.out.print(root.data + " ");
                root = root.right;
            }
            else{
                Node pre = root.left;
                while(pre.right != root && pre.right != null){
                    pre = pre.right;
                }
                if(pre.right == root){
                    pre.right = null;
                    root = root.right;
                }else{
                    System.out.print(root.data + " ");
                    pre.right = root;
                    root = root.left;
                }
            }
        }
    }

    public void preOrderItr(Node root){
        Deque<Node> stack = new LinkedList<Node>();
        stack.addFirst(root);
        while(!stack.isEmpty()){
            root = stack.pollFirst();
            System.out.print(root.data + " ");
            if(root.right != null){
                stack.addFirst(root.right);
            }
            if(root.left!= null){
                stack.addFirst(root.left);
            }
        }
    }
    
    public void postOrderItr(Node root){
        Deque<Node> stack1 = new LinkedList<Node>();
        Deque<Node> stack2 = new LinkedList<Node>();
        stack1.addFirst(root);
        while(!stack1.isEmpty()){
            root = stack1.pollFirst();
            if(root.left != null){
                stack1.addFirst(root.left);
            }
            if(root.right != null){
                stack1.addFirst(root.right);
            }
            stack2.addFirst(root);
        }
        while(!stack2.isEmpty()){
            System.out.print(stack2.pollFirst().data + " ");
        }
    }
    
    public void postOrderItrOneStack(Node root){
        Deque<Node> stack = new LinkedList<Node>();
        while(true){
            if(root != null){
                stack.addFirst(root);
                root = root.left;
            }else{
                if(!stack.isEmpty()){
                    if(stack.peekFirst().right != null){
                        root = stack.peekFirst().right;
                    }else{
                        root = stack.pollFirst();
                        System.out.print(root.data + " ");
                        while(!stack.isEmpty() && root == stack.peekFirst().right){
                            root = stack.pollFirst();
                            System.out.print(root.data + " ");
                        }
                        root = stack.isEmpty() ? null : stack.peekFirst().right;
                    }
                    
                }else{
                    break;
                }
            }
        }
    }
    
    public static void main(String args[]){
        BinaryTree bt = new BinaryTree();
        Node head = null;
        head = bt.addNode(1, head);
        head = bt.addNode(2, head);
        head = bt.addNode(3, head);
        head = bt.addNode(4, head);
        head = bt.addNode(5, head);
        head = bt.addNode(6, head);
        head = bt.addNode(7, head);
        head = bt.addNode(8, head);
        head = bt.addNode(9, head);
        head = bt.addNode(10, head);
        TreeTraversals tt = new TreeTraversals();
        
        /*tt.postOrder(head);
        System.out.println();
        tt.postOrderItr(head);
        System.out.println();
        tt.postOrderItrOneStack(head);*/
        
        tt.preOrder(head);
        //tt.preOrderItr(head);
        
    }
}