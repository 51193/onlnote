package icu.crepus.crepusserver;

public record BlogTreeNode(String path, String title, Boolean isFolder, BlogTreeData children) {
}
