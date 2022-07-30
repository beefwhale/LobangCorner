
# LobangCorner


MAD'22 Y2S1 Assignment LobangCorner

### Team Information

+ Celsius Chia Zhan Yao S10227778 
+ Wang Yongqing S10222015 
+ Chan Zi Xian S10204005 
+ Hasanah Samri S10222566 
+ Lai Yong Chuen S10222972 

### Description of App

<p>LobangCorner is a community for Singaporean food enthusiasts to share their favourite hawker stalls and recipes. With the theme of “Preserving Hawker Culture”, the main aim of the app is to help hawker businesses by bringing out the Singaporean spirit of sharing through this community. While “Hawker Corner” is the main focus of the app, we have added “Recipe Corner” as another supporting feature to make the app more robust, and to attract more users.</p>
  
<p>LobangCorner is a concept reused from CSF03’s Team Y FED Y1S2 project where it was used as the concept for the assignment website. The concept is inspired from [Our Grandfather Story](https://ourgrandfatherstory.com), and is made by Team Y’s member, Yongqing. This time, only small changes have been made to the concept such as cutting out “Delicacy Corner” and “Tour Corner” due to time constraints.</p>

### Design Decisions 

Font: Montserrat Regular, SemiBold, Bold <br>
<https://fonts.google.com/specimen/Montserrat#standard-styles>

#### Colour Palette  
- Primary : #ede9da Cream (Background) / #FAF9F6 Off White (Cards)  
- Secondary: #F6412D Red 
- Accent: #FFC100 Yellow 
- Text: Default Grey  

_No harsh outlines in app._

### About App Features

#### Login and Registration 

Users can create an account with LobangCorner using an email address that has not been registered. They can also create a username for each account they create.  

A typical login and registration layout was used. Only the logo of LobangCorner was added to the top. 

#### Home Page

Users can view a mix of Hawker Corner (HC) and Recipe Corner (RC) posts.

**[Stage 1]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Weekly Feature       | Post that is chosen randomly that refreshes every week. Can feature HC or RC posts.        |
| Explore              | Prompts users to explore HC and RC with its images and short description. Clickable ImageViews that can bring users to HC / RC. <br> Used hand-drawn styled images before adding a black low opacity colour infront for the text to pop. |
| Latest Posts | Shows users the latest 5 posts that have been published on LobangCorner. <br> Uses a different layout from Discover More section so the look of Home Page stays fresh and new. Cards also have a shadow to pop from the neutral background. |
| Discover More | Shows users up to 7 random posts and both HC and RC. <br> Uses the same card layout as hawker corner. A color identifier was added in the form of a line between the image and title in the card. The red represents HC and yellow represents RC. This identifier isn't explicitly stated to the users and is only intuitive. Cards also have a shadow to pop from the neutral background.   |
 
 **[Stage 2]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Bookmark Page        | The Bookmark Page appears when users click on the bookmark icon at the top right of the home page. It displays both the hawker and recipe posts that have been saved by the users. It contains a tab layout as well as a view pager that will be used by two fragments, recipe corner bookmark page and hawker corner bookmark page.         |
| Tab Layout           | The tab layout is used to toggle freely between both recipe corner bookmark page and hawker corner bookmark page. Users can either click on the tab name or swipe to switch tabs. However, once a hawker corner or a recipe post has been checked, users will not be able to click on the other tab or swipe to change to another tab. |
| HC Bookmark Page  | The Hawker Corner Bookmark Page displays all the hawker corner posts that have been saved by the users in a card format. Each card follows a similar layout to the one on the hawker corner main page and contains a checkbox at the top right of the card. Users can tick the checkboxes of the posts that they want to unsave. When 1 or more checkboxes are ticked, a red “unsave” button will appear at the bottom of the page and users will not be able to switch tabs to the recipe corner bookmark page.  |
| RC Bookmark Page  | Like the hawker corner bookmark page, the Recipe Corner Bookmark Page displays all the recipe corner posts that have been saved by the users in a card format. Each card follows a similar layout to the one on the recipe corner main page and contains a checkbox at the top right of the card. Users can tick the checkboxes of the posts that they want to unsave. Once one or more checkboxes are ticked, a red “unsave” button will appear at the bottom of the page and users will not be able to switch tabs to the hawker corner bookmark page.    |
| Save/Bookmark posts   | To save or bookmark a hawker or recipe post, users can go directly to the post from the main page or home page and click on the bookmark icon. In the hawker post page, the bookmark icon is placed on the right side of the view map button whereas in the recipe post page, the bookmark icon is placed on the top right of the page. Upon clicking, a toast message will be displayed, and the bookmark icon will be filled to indicate that the post has been saved successfully. The bookmark page will then be updated with the saved posts. To view all the saved posts, users can go to the bookmark page by clicking on the bookmark icon at the top right of the home page.      |
| Unsave/Unbookmark posts    | Users have two ways to unsave a hawker or recipe post. The first way is to go to the post itself and click on the filled bookmark icon. Upon clicking, a toast message will be displayed, and the bookmark icon will be unfilled to indicate that that the post has been unsaved. The bookmark page will be updated. The second way is to go to the bookmark page and tick the checkboxes of the posts that the users want to unsave. Afterwards, they can click on the “unsave” button that appears at the bottom of the page when one or more checkboxes are ticked. This will redirect them to the home page where a toast message will be displayed to indicate that the posts have been unsaved successfully. |
 
 #### Hawker Corner (HC)
 
Users can share stories or about the food of their favorite hawker stalls. Hawker owners can also promote their stall themselves.

 **[Stage 1]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Hawker Corner Main Page | A fragment that shows all stalls other users have created in a card format. Cards preview the stall name, an image of the stall, a short description about the stall and the user that created it. The stalls are retrieved from the database in real time and have a slight shadow background to pop up for aesthetic purposes. The fragment is loaded upon clicking the second icon in the navigation bar. There is also a search bar and drop-down sort bar that is explained in the bottom section. The user can choose a stall in the main page and view its information. Clicking the back button on the phone will return you to the hawker corner main page. The search bar and sort bar will stay at the top of the screen for ease of access until a certain point of scrolling, which then hides itself for more space to view the stalls for readability purpose. It will only reappear when you scroll back to the top of the stalls. If they want quick access back to the top, users can simply click out and in of hawker corner through the navigation bar. |
| Search bar | Allows users to search for a stall name or author (User that created the stall). This feature was added as there might be many stalls that is shown. This feature will allow user to find specific stalls that they have seen before, or to find out if a stall already exists before they create their own. At the same time, it also searches the authors of the stalls and users can make use of this to see stalls that their friends have recommended if they know their usernames. It can also be used to see if the stalls they created are in the list but this can also be found under the user profile section to find their own post. The search bar will show results of only the stalls with matching names or authors and shows no stall if there are none. When a user sees no stall, they might think that there is an error or lag, therefore, a toast message (notification) will appear when they enter their search, telling them there are no matches. |
| Sort bar | Provides user with the functionality of changing the order of the stalls. They can choose to view the stalls in ascending or descending order of the stalls’ name or authors’ name. This feature is implemented as the stalls are not in orderly manner and only shows the newest created stalls first. It can be useful to look through the stalls in alphabetical order to find certain stalls more easily. The sort bar will only show the sorting options when clicked on in a drop-down format. |
| Chosen stall page | This fragment appears when a user has chosen a stall from the main page. It will show the stalls name, a picture the author has uploaded, the user profile of the author, his/her username, a description of the author's experience with the stall, the address and finally, the opening hours of the stall. As the descriptions can be very long, the page can be scrolled. The stall name, address and opening hours will provide the user on information about the stall which can be utilized to visit the stall him/herself. The picture is used to show an example of the food, while the authors username and picture is displayed to show who created it. The description is where users can read on the author’s experience of the stall and decide whether he or she would like to visit this stall as well. |

 **[Stage 2]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Stall address in Maps         | To provide a general location of the stall, google maps has been implemented to show its location. Users can use the map in the app itself to check for its surroundings to locate it and see if they know how to get to the stall. If needed, users can also click on the “go there” button for the map to draw a line from their current location to the stall. If needed for clearer view and directions, the google maps toolbar is still enabled which will appear once the marker has been clicked. Users can directly go to google maps from the toolbar and will also show the location of the stall without the need of them retyping the address in. If the user wants to save the stall address, we also have an icon for them to copy the address to their clipboard.          |
| Comments           | Users are now able to leave comments on posts.  |
 
#### Recipe Corner (RC) 

Users can share their favorite or even their family recipes with the community. 

 **[Stage 1]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Recipe Corner Main Page | Shows users all the recipe posts that have been uploaded by other Lobang users. Each post previews the recipe's name, description, image, difficulty level (represented by yellow star ratings) and the user. It has a shadow to have a popping effect from the background. |
| Search bar | Allows users to search for the title of the recipe they wanted. It filters the recipes according to each letter being typed in the bar. If the word does not match any of the recipes listed, a toast of “No recipe found..” will be displayed. It has a shadow to have a popping effect from the background. 
| Spinner | Allows users to sort the list of recipes according to the difficulty level, title of recipe and the date when it is posted. This is to ease the burden of scrolling through every recipe to find what is needed. It has a shadow to have a popping effect from the background. 
| Recipe Corner Post Page | When the recipe post is clicked from the main page, it will lead to the recipe post page which shows the users a more detailed post and displays additional information like the recipe’s duration, ingredients and steps. The recipe title is colored in orange to make it stand out.  

  **[Stage 2]**
| Features             | Description and Purpose                       |
| -------------------- | ------------------------                      |
| Comments             | Users are now able to leave comments on posts.  |
 
#### Forms 

###### Hawker Forms

Users can give relevant inputs to post into the Hawker Corner to promote or advertise their desired Hawker Stall 
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Hawker Forms Page | The hawker forms page appears when you click on the floating button that is constantly on the bottom right corner of your screen above the nav bar. It prompts for information to be displayed on the hawker corner page, the information includes cover image, hawker stall name, a short and full description of the hawker stall, the address of the hawker stall, opening and closing hours, and days open (e.g., Mondays, Wednesdays, Fridays). After the user clicks submit, the given information are used to create a hawker class object and uploaded to the firebase. 
| Image Input | When the user clicks on the image input, their image folders will appear, then from their image folder, the user will select the image they want to use as the cover image for their hawker store. The design is a plus sign and a camera logo to show that you can add an image by clicking this logo. It is also grey to show that it is a prompt. 
| Opening and Closing Time Input | When the user clicks on the button for either the opening time or closing time, an alert dialog appears. In the alert dialog is a time picker, which uses a scrolling feature to scroll from 0 to 23 hours and 0 to 59 minutes. After scrolling to the desired hour and minute, the user will press ok, and the selected time will display on the button. The button was set to a yellow color button to distinguish from the submit button, which is red like the nav bar. 
| Days Open Input | When the user clicks on the “Select Open Days” button, an alert dialog will appear. In the alert dialog, there will be 7 checkboxes, each indicating a day of the week. The user can check some or all 7 of the checkboxes, depending on which day of the week the hawker stall is open. The alert dialog has a clear all button which unchecks all checked items, for efficiency when selecting open days. There is a down arrow on the right side of the button to indicate that it is a button and now a text input. 

###### Recipe Forms

Users can give relevant inputs to post into the Recipe Corner to share their desired food recipe.
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Recipe Forms Page | The recipe forms page appears when you click on the floating button that is constantly on the bottom right corner of your screen above the nav bar. It prompts for information to be displayed on the recipe corner page, the information includes cover image, recipe title, description, duration in minutes, difficulty ranging from 1 to 5, steps and ingredients required. After the user clicks submit, the given information are used to create a recipe class object and uploaded to the firebase. 
| Image Inputs | When the user clicks on the image input, their image folders will appear, then from their image folder, the user will select the image they want to use as the cover image for their hawker store. The design is a plus sign and a camera logo to show that you can add an image by clicking this logo. It is also grey to show that it is a prompt. 
| Difficulty Input | The difficulty input is a number picker ranging from 1 to 5. I used a number picker for efficiency, the user can just scroll between the 5 numbers and the number in between the divider will be the number taken as the input. 
| Tab Layout | A tab layout is used for the user to swipe from the main post to the steps or ingredients input. I used a tab layout as putting all the information into the main form page may make it look overwhelming. The color of the words are red with yellow lining under the words when the page is under the respective tab. This matches the overall color combination the app uses.  
| Steps Input | When users swipe to the steps tab, there will be a text input where they will enter the description of each step. A plus sign button is set beside the input so the user can click on the button to add it into the list of steps. There is also a button to remove the step that the user intends to remove 
| Ingredients Input | When users swipe to the ingredient tab, there will be a text input where they will enter each ingredient. A plus sign button is set beside the input so the user can click on the button to add it into the list of ingredients. There is also a button to remove the ingredient that the user intends to remove 

##### General Forms

**[Stage 2]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Draft Page  | The draft page appears when user click on either of the floating buttons that are constantly on the bottom right corner of you screen above the nav bar. If you click into the hawker floating button, the hawker draft page will appear, and when you click into recipe floating button, the recipe draft page will appear. In the draft pages, there is a plus button on the first item of the page, clicking it will bring you to a brand-new form, where you can create a new post. Below the plus button will show your drafts if there are drafts.   
| Delete Drafts  | In the draft page, it will show all the drafts that the user has. At the top right of each draft item, there will be a checkbox. The checkboxes are for when you want to delete the draft. A delete button is present at the top right corner of the draft page. After checking the checkbox and clicking on the delete button, the draft selected will be deleted permanently from your drafts. 
| Edit Drafts  | Clicking into the drafts will bring you to the form, with the inputs already filled into what was previously filled in. From there, you can either save to drafts again, or after filling in the inputs, you can submit your changes. 
| Save to Draft  | When in forms, after writing down some inputs and then leaving the page through the back button or the bottom nav bar, an alert dialog will pop up, asking whether the user wants to save, don’t save, or cancel. Saving will bring the user to the desired page and save the data to drafts. Not saving will bring the user to the desired page without saving, and cancel will dismiss the dialog and remain in forms. 

#### Profile
 
Users can share stories or about the food of their favorite hawker stalls. Hawker owners can also promote their stall themselves.

**[Stage 1]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Profile picture | The user can change their profile picture as they wish by clicking on the current profile picture and selecting a new one from their gallery. Profile picture is also made circular. 
| User hawker posts | The user can see the number of hawkers posts they have created, by clicking on it, it then displays the individual posts, after which they can further specify which post to view. It uses the same layout as HC.  
| User recipe posts | The user can see the number of recipes posts they have created, by clicking on it, it then displays the individual posts, after which they can further specify which post to view. It uses the same layout as RC. 
| Logout button | This is the only place by which the user is able to log out from, the user is constantly logged in until this button is pressed, allowing the user to not always having to log in. 

**[Stage 2]**
| Features             | Description and Purpose  |
| -------------------- | ------------------------ |
| Social Media Linking  | Users can link their social media accounts from Instagram/ Twitter/ Facebook. Facebook does not use account names to identify different accounts, so I have only allowed the Facebook profile link to be pasted into the EditText field. Meanwhile, Instagram and Twitter are redirected using usernames. If the user has the respective social media apps on their phone, they will be redirected to the app. Else, they will be linked to the web version instead.  Instagram and twitter are checked for valid characters while facebook is checked for a valid url. 
| Edit & Delete Posts  | Users are now able to edit and delete their posts. Users can delete more than 1 post at one go by selecting them using the checkbox. Pressing the delete icon at the top will ask users for confirmation of deletion. However, users can only edit one post at one go. If the user checks more than 1 post and attempts to edit by pressing the edit button at the top, they will be alerted and cancelling the alert will auto deselect all checked posts. When editing, current existing data will be passed into the forms for users to continue their edit from where they left off.    
| View other users Profile pages  | Users are now able to view other user’s profile pages by clicking on the author’s username in HC/RC posts. I have designed the username in a way that looks like a Hyperlink, that will effectively prompt users to explore by clicking on the username. Clicking on the username in the comment section does the same too. 



### User Guide

**[Stage 1]**
1. Create a new account with LobangCorner or Login with a preexisting account.  
2. Navigate to each section using the bottom navigation bar. First Icon is Home Page, followed by Hawker Corner, then Recipe Corner, then finally Profile Page. The Default page is Home.  
3. View existing posts on Home, HC or RC. 
4. Click on the red Floating Action Button at the bottom right corner of the screen to access the forms to make HC/ RC posts. 
5. Click on ‘Hawker Corner’ to make a Hawker Corner Post. Click on ‘Recipe Corner’ to make a Recipe Corner Post. 
6. To make a full recipe, don’t forget to swipe right to enter more things like ingredients and steps. 
7. You can also access your profile by pressing the first icon from the right of the bottom nav bar. 
8. In the profile section, tap on your photo to customize your profile photo. You can also customize the “About Me” section by directly clicking on the box itself. Don’t forget to press the Submit button after to save your changes.  
10. You can choose to Log Out by clicking the red bottom most button in profile page.  

**[Stage 2]**
1. Bookmarking: You can now bookmark (save) pages within HC and RC by clicking on the bookmark icon. A collection of your bookmarks can be found within Home page’s top right corner. To unsave a post, you can click the checked icon within the posts itself or unsave multiple posts in one go at the bookmark page using the checkbox and clicking the delete icon. 
2. Google Maps: When a user has found a hawker stall post interesting, they can click on the “View Map” button available just below the image of their chosen stall. This will bring you to a new page where you can view the location of the stall in Google Maps integrated into our app. If you are curious of which direction or how far you are from the stall, you can press the “Go There” button which will trigger the maps to get your location once permission is granted and draw a line between you and the stall. If you need the route and directions, simply click on the marker of the stall and a Google Map toolbar will appear where you can press to directly open the Google Maps Application and give you the information you need. 
3. Users can now leave comments within HC or RC. The author would receive notifications in turn.  
4. Users can now choose to save their unfinished post as a draft for future purposes. By clicking on the FAB, they would be led immediately to HC/RC drafts where they choose whether to make a new post, or to continue their previous works. 
5. Users can now edit and delete a post within their profile by clicking on the “hawker corner posts” or “recipe corner posts” square. Using the checkbox, they can delete more than 1 post at one go. 

### Roles and Contributions

**[Stage 1]**

Yongqing:
- Team Lead 
- Design Lead
- Home Page, Navigation Bar and Buttons, Small areas of profile page 
- Logo Design 
- Play Store Listing Designs  

Celsius: 
- Database Lead 
- Log in & Registration Page 
- User profile 

Zi Xian:  
- Coding of Hawker Corner main 
- Chosen stall 
- App demo during presentation 
- Incorporating design ideas into hawker corner and chosen stall 
- Retrieval of data from firebase 

Hasanah:  
- Coding of Recipe Corner Main  
- Coding of Chosen Recipe 
- Incorporating design ideas 

Yong Chuen:  
- Coding of Recipe Forms 
- Coding of Hawker Forms 
- Incorporating design ideas into forms 
- Uploading data obtained into firebase 


**[Stage 2]**

Yongqing:
- Team & Design Lead 
- Storyboarding & Drafting
- Documentation & Upload
- Allow users to access profiles of other users through HC and RC author usernames 
- Allow Linking of other social media in their profile (Twitter, Instagram, Facebook) 
- Allow Editing and Deleting of User’s own post 

Celsius: 
- Users are now able to leave comments on hawker and recipe posts 
- When a post has been commented on, the post creator receives a notification. 
- Our amazing aab exporter

Zi Xian:  
- Implemented Google Maps API for stalls 
- Allow users to get their current location to the stall 
- Loading screen animation and Incorporated design ideas 
- Google Play v2.0 descrption

Hasanah:  
- Allow users to view saved posts, save and unsave hawker or recipe corner posts 
- Allow Swipe-to-Refresh on Home page, Hawker Corner page, Recipe Corner page and Profile page 

Yong Chuen:  
- Allow users to save drafts after clicking on back button or bottom nav bar when an input field is filled. 
- Saved drafts can be deleted 

_LobangCorner brought to you by MAD P03 Team Y, AY22/23._
