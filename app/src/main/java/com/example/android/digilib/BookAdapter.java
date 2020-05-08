package com.example.android.digilib;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books)
    {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.booklistview, parent, false);
        }

        Book currentBook = getItem(position);

        TextView booknameTextView = (TextView) listItemView.findViewById(R.id.book_name);
        Log.i("BOOK ADAPTER", "current Book: " + currentBook.getBookName());
        booknameTextView.setText(currentBook.getBookName());

        TextView bookauthorTextView = (TextView) listItemView.findViewById(R.id.author_name);
        bookauthorTextView.setText(currentBook.getAuthors());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentBook.getPublishedDate());

        ImageView bookImage = (ImageView) listItemView.findViewById(R.id.book_image);
        if(!currentBook.getImageLink().isEmpty())
        Picasso.get().load(currentBook.getImageLink()).into(bookImage);

//        ImageView bookImage = (ImageView) listItemView.findViewById(R.id.book_image);
//        bookImage.setImageDrawable();
//
        return listItemView;
    }
}
