package com.example.android.digilib;

public class Book {

    private String mBookName;

    private String mBookLink;

    private String mImageLink;

    private String[] Authors;

    private String mPublishedDate;

    private String mAuthors;

    public Book(String bookname, String[] authors, String date, String imagelink,  String booklink)
    {
        mBookName = bookname;
        Authors = authors;
        mPublishedDate = date;
        mImageLink = imagelink;
        mBookLink = booklink;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getBookLink() {
        return mBookLink;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public String getAuthors() {

        if(Authors.length == 1)
            mAuthors = "Author: ";
        else
            mAuthors = "Authors: ";
        for(int i=0;i<Authors.length;i++)
            if(i == (Authors.length-1))
                mAuthors = mAuthors + Authors[i];
            else
                mAuthors = mAuthors + Authors[i] + ", ";

        return mAuthors;
    }
}
