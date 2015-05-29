#include "usart.h"

/*
 * Functie de initializare a controllerului USART
 */
void USART0_init() 
{
    /* seteaza baud rate la 115200 */
    UBRR0H = 0;
    UBRR0L = 8;

    /* porneste transmitatorul */
    UCSR0B = (1<<TXEN0) | (1<<RXEN0);

    /* seteaza formatul frame-ului: 8 biti de date, 1 bit de stop, fara paritate */
    UCSR0C = (0<<USBS0) | (3<<UCSZ00);
}

/*
 * Functie ce transmite un caracter prin USART
 *
 * @param data - caracterul de transmis
 */
void USART0_transmit(char data) 
{
    /* asteapta pana bufferul e gol */
	
    while(!(UCSR0A & (1<<UDRE0)));

    /* pune datele in buffer; transmisia va porni automat in urma scrierii */
    UDR0 = data;
}

/*
 * Functie ce primeste un caracter prin USART
 *
 * @return - caracterul primit
 */
char USART0_receive() 
{
    /* asteapta cat timp bufferul e gol */

    //while(!(UCSR0A & (1<<RXC0)));
	if (!(UCSR0A & (1<<RXC0)))
		return '\0';
	
    /* returneaza datele din buffer */
    return UDR0;
}

/*
 * Functie ce transmite un sir de caractere prin USART
 *
 * @param data - sirul (terminat cu '\0') de transmis
 */
void USART0_print(char *data) 
{
    while(*data != '\0')
        USART0_transmit(*data++);
}
