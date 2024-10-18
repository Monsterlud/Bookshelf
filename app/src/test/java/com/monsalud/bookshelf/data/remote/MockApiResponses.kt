package com.monsalud.bookshelf.data.remote

val listResponse =
    """          
        {
          "status": "OK",
          "copyright": "Copyright (c) 2019 The New York Times Company.  All Rights Reserved.",
          "num_results": 1,
          "last_modified": "2016-03-11T13:09:01-05:00",
          "results": [
            {
              "list_name": "Hardcover Fiction",
              "display_name": "Hardcover Fiction",
              "bestsellers_date": "2016-03-05",
              "published_date": "2016-03-20",
              "rank": 5,
              "rank_last_week": 2,
              "weeks_on_list": 2,
              "asterisk": 0,
              "dagger": 0,
              "amazon_product_url": "http://www.amazon.com/Girls-Guide-Moving-On-Novel-ebook/dp/B00ZNE17B4?tag=thenewyorktim-20",
              "isbns": [
                {
                  "isbn10": 553391925,
                  "isbn13": "9780553391923"
                }
              ],
              "book_details": [
                {
                  "title": "A GIRL'S GUIDE TO MOVING ON",
                  "description": "A mother and her daughter-in-law both leave unhappy marriages and take up with new men.",
                  "contributor": "by Debbie Macomber",
                  "author": "Debbie Macomber",
                  "contributor_note": "",
                  "price": 0,
                  "age_group": "",
                  "publisher": "Ballantine",
                  "primary_isbn13": "9780553391923",
                  "primary_isbn10": 553391925
                }
              ],
              "reviews": [
                {
                  "book_review_link": "",
                  "first_chapter_link": "",
                  "sunday_review_link": "",
                  "article_chapter_link": ""
                }
              ]
            }
          ]
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
