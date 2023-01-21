import pickle
from pathlib import Path


class Bouquet:
    """Create a new bouquet for the store"""
    def __init__(self, name, flowers_list):
        """Take as arguments bouquet name and list of flowers
        to create a bouquet object

        """
        self.name = name
        self.flowers_list = flowers_list

    def price(self):
        """Calculate the total price of the bouquet.

        Iterate through flowers in the bouquet, multiply
        their price on their quantity and add it to the
        price variable.  Return price.

        """
        price = 0
        for flower in self.flowers_list:
            price += flower.price * flower.quantity
        return price

    def description(self):
        """Show description of the bouquet.

        Show the name of the bouquet, names and amount of flowers
        in the bouquet, price of each flower and total price of the
        bouquet

        """
        print('')
        print('Bouquet "' + self.name + '" is consists of following flowers:')
        print('')
        for flower in self.flowers_list:
            print(flower.name + '(' + str(flower.quantity) + '): color - ' +
                  flower.color + ', length - ' + str(flower.length) +
                  ', price for each - ' + str(flower.price) + '$')
        print('')
        print('Total price of the bouquet: ' + str(self.price()) + '$')

    def save(self):
        """Save bouquet to database.

        Save bouquet to database.  Notify the user whether the operation
        succeeded or failed.

        """
        try:
            bouquet_path = Path('./database/bouquets/' + self.name + '.p')
            with open(bouquet_path, 'wb') as file:
                pickle.dump(self, file)
            print('')
            print('Bouquet "' + self.name + '" is saved!')
            print('')
        except OSError:
            print('Failed to save the bouquet')

    def remove(self):
        Path('./database/bouquets/' + self.name + '.p').unlink()
        print('')
        print('Bouquet ' + self.name + ' has been removed!')
        print('')

    def buy(self):

        def load_flower(name):
            path = Path('./database/flowers/' + name + '.p')
            with open(path, 'rb') as file:
                flower = pickle.load(file)
                return flower

        flowers = []
        for flower in self.flowers_list:
            try:
                db_flower = load_flower(flower.name)
            except OSError:
                print('Flowers in this bouquet are not available right now')
                return
            db_flower.quantity -= flower.quantity
            if db_flower.quantity < 0:
                print('Sry, we don`t have enough flowers :(')
                return
            flowers.append(db_flower)
        for flower in flowers:
            flower.save()
        print('')
        print('Bouquet "' + self.name + '" has been purchased for $' + str(self.price()))
