"""Program for managing Flower store.

As administrator program creates new flowers and bouquets,
saves them to external byte files with pickle (these files
will be referred to as "database" in the future).  It also
removes those flowers and bouquets from database, deleting
byte files.  Updates current stock of flowers if necessary.

As a customer program shows available bouquets, their price,
tells what flowers are included in bouquets and gives their
description.  If customer buys a bouquet, program subtracts
amount of flowers in the bouquet from amount of flowers in the
stock.

"""
import pickle
from pathlib import Path

import flowerclass as f
import bouquetclass as b


# exec(open("__main__.py").read())
"""Start of database interactions code.

Functions for loading all flowers from database,
for loading a singular flower using flower name as argument,
for loading all bouquets from database, for saving flowers
and bouquets to database.

"""


def load_flowers_list():
    """Load a list of all flowers in database.

    Create a variable "flowers" for a list of flower objects.
    Iterate through all files in database folder containing
    flowers, load flower object from each file and append it
    to "flowers", return "flowers" list.  If loading failed,
    notify user with a message.

    """
    flowers = []
    try:
        files = Path('./database/flowers').glob('*.p')
        for file in files:
            with open(file, 'rb') as file:
                flowers.append(pickle.load(file))
        return flowers
    except OSError:
        print('Failed to load flowers list')


def load_flower(name):
    """Load a singular flower from database.

    When user enters a flower name, use his input
    to check if there is a flower with such name
    in database.  If it exists, load that flower.
    If not, notify user that there is no flower with
    that name.

    """
    if Path('./database/flowers/' + name + '.p'):
        path = Path('./database/flowers/' + name + '.p')
        with open(path, 'rb') as file:
            flower = pickle.load(file)
            return flower
    else:
        print('No such flower!')


def load_bouquets_list():
    """Load a list of all bouquets.

    Create a list "bouquets", iterate through all files
    in database folder containing bouquets, load a bouquet
    object form each file and append it to "flowers".
    Return "flowers" list. If loading failed, notify user
    about it.

    """
    bouquets = []
    try:
        paths = Path('./database/bouquets').glob('*.p')
        for path in paths:
            with open(path, 'rb') as file:
                bouquets.append(pickle.load(file))
        return bouquets
    except OSError:
        print('Failed to load bouquets list')


"""End of database interactions code"""


"""Start of the menu code"""


def login():
    """Log in menu.

    First menu when starting the program.  User chooses between
    2 regimes of the program, one for the administrator and another
    one for customers.  This affects the functionality of the program.
    Also user can exit the program, because otherwise there will be an
    infinite loop of this function, until user enters correct value.

    """
    print('')
    print('Hello, welcome to the Flower Store!')
    print('')
    print('type "1" if u want to enter as the administrator')
    print('type "2" if u want to enter as a customer')
    print('')
    print('type "0" if u want to exit the program')
    print('')
    user_login = input('Log in, pls: ')
    if user_login == '1':
        admin_menu()
        login()
    elif user_login == '2':
        customer_menu()
        login()
    elif user_login == '0':
        return
    else:
        print('Wrong input! try again.')
        login()


def admin_menu():
    """Admin menu.

    Run this function if user chose "administrator"
    option in "log in menu".  It contains options for
    creating and removing flowers and bouquets, and an
    option to update the stock of flowers in database.
    Get user back to "log in menu" if he types "0".

    """
    print('')
    print('Welcome, administrator')
    print('')
    print('type "1" if u want to create new bouquet')
    print('type "2" if u want to remove a bouquet')
    print('type "3" if u want to change how many flowers are in stock')
    print('type "4" if u want to add a new flower')
    print('type "5" if u want to remove a flower')
    print('')
    print('type 0 if u want to get back to log-in menu')
    print('')
    admin_input = input('Choose what u want to do: ')
    if admin_input == '1':
        create_new_bouquet()
    elif admin_input == '2':
        remove_bouquet()
    elif admin_input == '3':
        flowers_stock()
    elif admin_input == '4':
        add_flower()
    elif admin_input == '5':
        remove_flower()
    elif admin_input == '0':
        return
    else:
        print('')
        print('Wrong input! try again')

    admin_menu()


def create_new_bouquet():
    """Create a new bouquet and save it to database.

    Load all flowers objects from database.  Keep it in variable for
    "add_flower_bouquet" function.  Create an empty list for adding
    flowers for the new bouquet.  Take a bouquet name input from
    user. Return to "admin_menu" if user types "cancel" in place of
    a name of the bouquet. Run "add_flower" function in a loop, until
    user is done with adding flowers to the bouquet.

    """
    flowers = load_flowers_list()
    new_bouquet_flowers = []
    print('')
    print('Let`s create a new bouquet!')
    print('')
    print('type "cancel" if u want to get back at any point')
    print('')
    bouquet_name = input('Enter new bouquet name: ')
    if bouquet_name == 'cancel':
        return
    print('')

    def current_bouquet(flowers_list):
        """Show current flowers in a new bouquet.

        Iterate through list of flowers in a bouquet user is creating
        and calculate their total price.  Display this info to user.

        """
        print('')
        print('Your current bouquet "' + bouquet_name + '" is consists of:')
        bouquet_price = 0
        for flower in flowers_list:
            print(flower.name + ': ' + str(flower.quantity))
            bouquet_price += flower.price * flower.quantity
        print('')
        print('total price: ' + str(bouquet_price) + '$')
        print('')
        print('type "done" if u want to save bouquet and add it to the store')

    def add_flower_bouquet():
        """Add a flower to the bouquet user is creating.

        Iterate through available flowers from database, display them
        to user.  Take 2 inputs from user: name of the flower he
        wants to add to the bouquet and quantity of that flower.
        Load flower object from database, change quantity to user's
        entered quantity and append that object to the "new_bouquet_flowers"
        list.  If user already added flowers to the bouquet he is creating,
        display list and price of those flowers.
        Save new bouquet to database when user types "done"
        Return to "create_new_bouquet_menu" if user types "cancel"

        """
        if new_bouquet_flowers:
            current_bouquet(new_bouquet_flowers)
        print('Type "cancel" to get back')
        print('')
        print('Available flowers are:')
        print('')
        for flower in flowers:
            flower.description()
        print('')
        flower_name = input('Enter flower name to add it to the bouquet "' +
                            bouquet_name + '": ')
        if flower_name == 'cancel':
            return
        if flower_name == 'done' and new_bouquet_flowers:
            new_bouquet = b.Bouquet(bouquet_name, new_bouquet_flowers)
            new_bouquet.save()
            return
        print('')
        flower_quantity = input('Enter how many of the flower "' +
                                flower_name + '" u want to add: ')
        try:
            new_flower = load_flower(flower_name)
        except OSError:
            print('Wrong flower name!')
            return
        try:
            new_flower.quantity = int(flower_quantity)
        except ValueError:
            print('Flower quantity must be a number!')
            return
        if new_flower.quantity < 0:
            print('Flower quantity must be a non-negative number!')
            return
        new_bouquet_flowers.append(new_flower)
        print(str(new_flower.quantity) + ' of the flower "' +
              new_flower.name + '" has been added to the bouquet')

        add_flower_bouquet()

    add_flower_bouquet()


def remove_bouquet():
    """Remove an existing bouquet from database.

    Load all bouquets from database, display their names to
    user and take input from him (bouquet name he wants to remove).
    Check if the bouquet with such name exists in database.  Remove
    that bouquet from database if it exists, notify the user if it
    doesnt exist.

    """
    bouquets_list = load_bouquets_list()
    print('')
    print('Here are existing bouquets in the store')
    for bouquet in bouquets_list:
        print(bouquet.name)
    print('')
    print('Enter a bouquet name to remove it or')
    print('Type "0" to get back')
    bouquet_name = input('Enter a bouquet name u wish to remove: ')
    if bouquet_name == '0':
        return
    for bouquet in bouquets_list:
        if bouquet.name == bouquet_name:
            bouquet.remove()
        else:
            print('No such bouquet!')


def flowers_stock():
    """Change the amount of flowers in the stock.

    Load list of all flowers from database, display their description
    (name, color, length, price, amount) to user.  Get from user a
    flower name to update its stock.  Get input from user (amount of flowers
    he wants to add or remove(negative number to remove)).  Load flower
    object from database, change its quantity and save it back to database.
    If quantity of a flower drops to below zero, set to 0 instead and
    notify user.

    """
    flowers = load_flowers_list()
    print('')
    print('Current stock of flowers:')
    print('')
    for flower in flowers:
        flower.description()
    print('')
    print('type "0" if u want to get back')
    print('')
    name = input('Enter flower name to change its stock: ')
    if name == '0':
        return
    print('')
    try:
        quantity = int(input('Number of flowers u want to add or remove: '))
    except ValueError:
        print('Quantity must be a number!')
        return
    if int(quantity) < 0:
        print('Quantity must be a non-negative number!')
        return
    flower = load_flower(name)
    flower.update(quantity)

    flowers_stock()


def add_flower():
    """Add a new flower to database.

    Take 5 inputs from user (name, color, length, price, quantity),
    then create new flower object using those inputs.  Save new flower
    to database.

    """
    print('')
    print('Let`s add a new flower to the store!')
    print('')
    print('To cancel at any point type "cancel"')
    print('')
    name = input('Enter new flower name: ')
    if name == 'cancel':
        return
    color = input('Enter new flower color: ')
    if color == 'cancel':
        return
    length = input('Enter new flower length: ')
    if length == 'cancel':
        return
    price = input('Enter new flower price: ')
    if price == 'cancel':
        return
    quantity = input('Enter new flower quantity: ')
    if quantity == 'cancel':
        return
    try:
        flower = f.Flower(name, color, int(length), int(price),
                          int(quantity))
        if flower.length < 0 or flower.price < 0 or flower.quantity < 0:
            print('Length, price and quantity must be a non-negative number!')
            return
        flower.save()
        print('')
        print('New flower "' + flower.name + '" is saved!')
        print('')
    except OSError:
        print('Failed to add a flower :(')
        return
    except ValueError:
        print('Wrong input! Length, price and quantity must be a number!')


def remove_flower():
    """Remove a flower from database.

    Load list of all flowers from database, show their description
    (name, color, length, price, quantity), take an input from
    user (name of the flower he wants ro remove).  Check if flower
    with that name exists in database.  If it does, remove it from
    database, if not - notify the user.

    """
    flowers = load_flowers_list()
    print('')
    print('Current flowers available in the store are:')
    print('')
    for flower in flowers:
        flower.description()
    print('')
    print('type "0" if u want to get back')
    print('')
    name = input('Enter a flower name u want to remove: ')
    if name == '0':
        return
    try:
        flower = load_flower(name)
        flower.remove()
    except OSError:
        print('No such flower!')


def customer_menu():
    """Customer menu to view and buy bouquets of flowers.

    Load bouquets from database, show their names and price to user.
    Take input from user (bouquet name he wants to view).  Iterate
    through list of bouquets from database, check if there is a bouquet
    with the name user entered.  If there is, show description of that
    bouquet (list of flowers, price).  If user wants to buy bouquet,
    run buy_bouquet function to update stock of flowers.

    """
    bouquets = load_bouquets_list()
    print('')
    print('Hello, customer. Here are available bouquets for sale.')
    print('')
    for bouquet in bouquets:
        print(bouquet.name + ': ' + str(bouquet.price()) + '$')
    print('')
    print('Or type "0" if u want to get back to log-in menu')
    print('')
    customer_input = input('Enter bouquet`s name to see the details: ')
    if customer_input == '0':
        return
    for bouquet in bouquets:
        if bouquet.name == customer_input:
            bouquet.description()
            print('')
            buy_input = input('Type "buy" to buy this bouquet: ')
            if buy_input == 'buy':
                bouquet.buy()

    customer_menu()


if __name__ == '__main__':
    login()
