package com.monsalud.bookshelf.data.remote

val listResponse =
    """          
{
  "status": "OK",
  "copyright": "Copyright (c) 2019 The New York Times Company.  All Rights Reserved.",
  "num_results": 15,
  "last_modified": "2015-12-25T13:05:20-05:00",
  "results": {
    "list_name": "Trade Fiction Paperback",
    "bestsellers_date": "2015-12-19",
    "published_date": "2016-01-03",
    "display_name": "Paperback Trade Fiction",
    "normal_list_ends_at": 10,
    "updated": "WEEKLY",
    "books": [
      {
        "rank": 1,
        "rank_last_week": 0,
        "weeks_on_list": 60,
        "asterisk": 0,
        "dagger": 0,
        "primary_isbn10": 553418025,
        "primary_isbn13": "9780553418026",
        "publisher": "Broadway",
        "description": "Separated from his crew, an astronaut embarks on a quest to stay alive on Mars. The basis of the movie.",
        "price": 0,
        "title": "THE MARTIAN",
        "author": "Andy Weir",
        "contributor": "by Andy Weir",
        "contributor_note": "",
        "book_image": "http://du.ec2.nytimes.com.s3.amazonaws.com/prd/books/9780804139038.jpg",
        "amazon_product_url": "http://www.amazon.com/The-Martian-Novel-Andy-Weir-ebook/dp/B00EMXBDMA?tag=thenewyorktim-20",
        "age_group": "",
        "book_review_link": "",
        "first_chapter_link": "",
        "sunday_review_link": "",
        "article_chapter_link": "",
        "isbns": [
          {
            "isbn10": 804139024,
            "isbn13": "9780804139021"
          }
        ]
      }
    ],
    "corrections": []
  }
}
    """

val reviewResponse =
    """
{
  "status": "OK",
  "copyright": "Copyright (c) 2019 The New York Times Company.  All Rights Reserved.",
  "num_results": 2,
  "results": [
    {
      "url": "http://www.nytimes.com/2011/11/10/books/1q84-by-haruki-murakami-review.html",
      "publication_dt": "2011-11-10",
      "byline": "JANET MASLIN",
      "book_title": "1Q84",
      "book_author": "Haruki Murakami",
      "summary": "In “1Q84,” the Japanese novelist Haruki Murakami writes about characters in a Tokyo with two moons.",
      "isbn13": [
        "9780307476463"
      ]
    }
  ]
}
    """
