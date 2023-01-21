import pickle
from pathlib import Path


class Flower:
    """Class for creating new flowers
    Create a new flower for the store."""
    def __init__(self, name, color, length, price, quantity):
        """Take 5 arguments and use them to create a new flower object."""
        self.name = name
        self.color = color
        self.length = length
        self.price = price
        self.quantity = quantity

    def description(self):
        """Shows name, color, length, quantity and price of the flower."""
        print(self.name + ': quantity - ' + str(self.quantity) +
              ', color - ' + self.color + ', length - ' + str(self.length) +
              '. Price: ' + str(self.price) + '$')

    def save(self):
        """Save flower to database.

        Save flower to database.  If failed, notify the user.

        """
        try:
            flower_path = Path('./database/flowers/' + self.name + '.p')
            with open(flower_path, 'wb') as file:
                pickle.dump(self, file)
        except OSError:
            print('Failed to save the flower')

    def remove(self):
        if Path('./database/flowers/' + self.name + '.p'):
            Path('./database/flowers/' + self.name + '.p').unlink()
            print('')
            print('Flower "' + self.name + '" has been removed!')

    def load_flower(self):
            path = Path('./database/flowers/' + self.name + '.p')
            with open(path, 'rb') as file:
                flower = pickle.load(file)
                return flower

    def update(self, quantity):
        flower = self.load_flower()
        flower.quantity += quantity
        if flower.quantity < 0:
            flower.quantity = 0
            print('Not enough flowers! Quantity set to 0')
        flower.save()
        print('Stock updated! There is now ' + str(flower.quantity) +
          ' of the flower "' + flower.name + '" in stock.')


"""Examples for database:

Rose = Flower('rose','red',20,5,20)
Jasmine = Flower('jasmine', 'white', 10, 7, 20)
Tulip = Flower('tulip','yellow',15,3,20)
Lily = Flower('lily','white',10,6,20)
Orchid = Flower('orchid','red',12,4,20)

"""
