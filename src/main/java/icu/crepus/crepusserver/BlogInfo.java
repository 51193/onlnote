package icu.crepus.crepusserver;

import java.util.ArrayList;

public record BlogInfo(String path, String title, ArrayList<BlogFileInfo> blogFileInfos) {
}
