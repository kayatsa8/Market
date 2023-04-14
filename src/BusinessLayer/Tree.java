package BusinessLayer;

import java.util.ArrayList;
import java.util.List;

public class Tree
{
    private Node root;
    public Tree(int rootData) {
        root = new Node(rootData);
    }

    public void removeNode(int data) {
        Node nodeToRemove = findNode(data, root);
        if (nodeToRemove == null) {
            return;
        }
        Node parent = findParent(data, root);
        if (parent != null) {
            parent.removeChild(nodeToRemove);
        } else {
            System.out.println("Can't remove the founder of the store");
        }
    }

    // helper method to find the parent node of a node with the given data value
    private Node findParent(int data, Node node) {
        if (node == null || node.children.isEmpty()) {
            return null;
        }
        for (Node child : node.children) {
            if (child.data == data) {
                return node;
            } else {
                Node result = findParent(data, child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    // method to add new node to tree
    public void addNode(int parentData, int newData) {
        Node parent = findNode(parentData, root);
        if (parent != null) {
            Node newNode = new Node(newData);
            parent.addChild(newNode);
        }
    }

    // helper method to find a node in the tree
    private Node findNode(int ID, Node node) {
        if (node == null) {
            return null;
        }
        if (node.data == ID) {
            return node;
        }
        for (Node child : node.children) {
            Node result = findNode(ID, child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // method to print tree in pre-order traversal
    public void printTree(Node node) {
        System.out.print(node.data + " ");
        for (Node child : node.children) {
            printTree(child);
        }
    }
    public List<Integer> getTreeDataAsList()
    {
        List<Integer> result = new ArrayList<>();
        getTreeDataAsListRecursive(root, result);
        return result;
    }
    private void getTreeDataAsListRecursive(Node node, List<Integer> result)
    {
        result.add(node.getData());
        for (Node child : node.children) {
            getTreeDataAsListRecursive(child, result);
        }
    }
    public Node getRoot()
    {
        return root;
    }

    public class Node {
        private int data;
        public List<Node> children;

        public Node(int data) {
            this.data = data;
            children = new ArrayList<>();
        }

        public void addChild(Node node) {
            children.add(node);
        }
        public void removeChild(Node node) {
            children.remove(node);
        }
        public int getData()
        {
            return data;
        }
    }
}
