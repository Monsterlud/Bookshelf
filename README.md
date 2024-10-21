# Bookshelf

**Bookshelf** is your window into the Best Seller Lists of the New York Times. This app uses the New York Times Books API to retrieve the latest books on the most popular Best Seller lists: Fiction, Nonfiction, Culture, Food & Diet, Graphic Books & Manga, Politics & American History, Science, Sports & Fiteness, and Travel. 

## Welcome
A welcome screen introduces the User to the app and gives them simple information about what to expect. Clicking out of this dialog triggers a change in a preference that indicates to the app whether to show or not show this information. I used Preferences DataStore to implement shared preferences.

![bookshelf_welcome](images/bookshelf_welcome)

## Navigation
A navigation drawer gives you access to all of the lists. Clicking on any of them navigates you to a list view of Best Selling Books for that subject. All UI in this app are created using Jetpack Compose. All navigation is using the latest type-safe Compose Navigation.

![bookshelf_navigation](images/bookshelf_navigation)


## Lists
The Compose Lazy List that displays on the BookshelfListScreen is populated with the latest NYT data with a network call that also saves the data to a local Room Database. This screen shows a CircularProgressIndicator while the loading is happening. 

![bookshelf_list_light](images/bookshelf_list_light)
![bookshelf_list_dark](images/bookshelf_list)

The items in the list get their data (title, author, image, etc) from the network call response, which is parsed into an Entity that the Room Database can handle and the UI can use. Clicking on any item navigates the User to a detail screen.

## Book Details
The BookReviewScreen is the detail view which gets some of its data passed from the List screen and some from a separate network call to the Review API. Additional information is seen here such as ranking info, publisher, description of the book, and links (if they exist) for a book review and an amazon link). These links open in a browser window.

