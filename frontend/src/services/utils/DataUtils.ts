
export const dateToString = (date: any) => new Date(date).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
export const numberToString = (value: any) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
export const numberToStringPerc = (value: any) => new Intl.NumberFormat('pt-BR', { style: 'percent' }).format(value);