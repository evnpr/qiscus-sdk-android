/*
 * Copyright (c) 2016 Qiscus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiscus.sdk.filepicker;

import com.qiscus.sdk.R;
import com.qiscus.sdk.filepicker.model.BaseFile;
import com.qiscus.sdk.filepicker.model.FileType;

import java.util.ArrayList;

/**
 * Created on : March 16, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public enum PickerManager {
    INSTANCE;
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private int currentCount;
    private PickerManagerListener pickerManagerListener;

    private ArrayList<String> mediaFiles;
    private ArrayList<String> docFiles;
    private ArrayList<FileType> fileTypes;

    private boolean showVideos;
    private boolean showGif;
    private boolean docSupport = true;
    private boolean enableOrientation = false;
    private boolean showFolderView = true;

    public static PickerManager getInstance() {
        return INSTANCE;
    }

    PickerManager() {
        mediaFiles = new ArrayList<>();
        docFiles = new ArrayList<>();
        fileTypes = new ArrayList<>();
    }

    public void setMaxCount(int count) {
        clearSelections();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setPickerManagerListener(PickerManagerListener pickerManagerListener) {
        this.pickerManagerListener = pickerManagerListener;
    }

    public void add(String path, int type) {
        if (path != null && shouldAdd()) {
            if (!mediaFiles.contains(path) && type == FilePickerConst.FILE_TYPE_MEDIA) {
                mediaFiles.add(path);
            } else if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
                docFiles.add(path);
            } else {
                return;
            }

            currentCount++;

            if (pickerManagerListener != null) {
                pickerManagerListener.onItemSelected(currentCount);

                if (maxCount == 1) {
                    pickerManagerListener.onSingleItemSelected(type == FilePickerConst.FILE_TYPE_MEDIA ?
                            getSelectedPhotos() : getSelectedFiles());
                }
            }
        }
    }

    public void remove(String path, int type) {
        if ((type == FilePickerConst.FILE_TYPE_MEDIA) && mediaFiles.contains(path)) {
            mediaFiles.remove(path);
            currentCount--;
        } else if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
            docFiles.remove(path);
            currentCount--;
        }

        if (pickerManagerListener != null) {
            pickerManagerListener.onItemSelected(currentCount);
        }
    }

    public boolean shouldAdd() {
        return currentCount < maxCount;
    }

    public ArrayList<String> getSelectedPhotos() {
        return mediaFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void clearSelections() {
        docFiles.clear();
        mediaFiles.clear();
        fileTypes.clear();
        currentCount = 0;
        maxCount = 0;
    }

    public boolean showVideo() {
        return showVideos;
    }

    public void setShowVideos(boolean showVideos) {
        this.showVideos = showVideos;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

    public void addFileType(FileType fileType) {
        fileTypes.add(fileType);
    }

    public void addDocTypes() {
        String[] pdfs = {"pdf"};
        fileTypes.add(new FileType(FilePickerConst.PDF, pdfs, R.drawable.ic_qiscus_pdf));

        String[] docs = {"doc", "docx", "dot", "dotx"};
        fileTypes.add(new FileType(FilePickerConst.DOC, docs, R.drawable.ic_qiscus_word));

        String[] ppts = {"ppt", "pptx"};
        fileTypes.add(new FileType(FilePickerConst.PPT, ppts, R.drawable.ic_qiscus_ppt));

        String[] xlss = {"xls", "xlsx"};
        fileTypes.add(new FileType(FilePickerConst.XLS, xlss, R.drawable.ic_qiscus_excel));

        String[] txts = {"txt"};
        fileTypes.add(new FileType(FilePickerConst.TXT, txts, R.drawable.ic_qiscus_txt));
    }

    public ArrayList<FileType> getFileTypes() {
        return fileTypes;
    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }

    public boolean isEnableOrientation() {
        return enableOrientation;
    }

    public void setEnableOrientation(boolean enableOrientation) {
        this.enableOrientation = enableOrientation;
    }
}
