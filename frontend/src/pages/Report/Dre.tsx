import { Template } from "../../components/Template/Template";
import { useEffect, useState } from "react";
import { http } from '../../services/api';
import { useAuth } from "../../contexts/AuthContext";
import { numberToString, numberToStringPerc } from "../../services/utils/DataUtils";

type DRE = {
    categoryId: string;
    name: string;
    type: string;
    
    jan: string;
    feb: string;
    mar: string;
    apr: string;
    may: string;
    june: string;
    july: string;
    aug: string;
    sept: string;
    oct: string;
    nov: string;
    dec: string;

    janPerc: string;
    febPerc: string;
    marPerc: string;
    aprPerc: string;
    mayPerc: string;
    junePerc: string;
    julyPerc: string;
    augPerc: string;
    septPerc: string;
    octPerc: string;
    novPerc: string;
    decPerc: string;
}
type Saldo = {
    jan?: string | number;
    feb?: string | number;
    mar?: string | number;
    apr?: string | number;
    may?: string | number;
    june?: string | number;
    july?: string | number;
    aug?: string | number;
    sept?: string | number;
    oct?: string | number;
    nov?: string | number;
    dec?: string | number;
}


export function DRE() {
    const [year, setYear] = useState(2023); 
    
    const [totalExpense, setTotalExpense] = useState<Saldo>({});
    const [totalRevenue, setTotalRevenue] = useState<Saldo>({});
    const [saldo, setSaldo] = useState<Saldo>({});

    const [dreExpense, setDreExpense] = useState<DRE[]>([]);
    const [dreRevenue, setDreRevenue] = useState<DRE[]>([]);

    const { user } = useAuth();

    const fetchDRE = async () => {
        const response = await http.get(`/users/${user?.userId}/dre`);

        const expense = response.data.filter((item: any) => item.type === 'EXPENSE');
        const revenue = response.data.filter((item: any) => item.type === 'REVENUE');

        const totalRev = revenue.reduce((acc: any, a: any) => ({
            jan: acc.jan + a.jan,
            feb: acc.feb + a.feb,
            mar: acc.mar + a.mar,
            apr: acc.apr + a.apr,
            may: acc.may + a.may,
            june: acc.june + a.june,
            july: acc.july + a.july,
            aug: acc.aug + a.aug,
            sept: acc.sept + a.sept,
            oct: acc.oct + a.oct,
            nov: acc.nov + a.nov,
            dec : acc.dec + a.dec,
        }));

        const totalExp = expense.reduce((acc: any, a: any) => ({
            jan: acc.jan + a.jan,
            feb: acc.feb + a.feb,
            mar: acc.mar + a.mar,
            apr: acc.apr + a.apr,
            may: acc.may + a.may,
            june: acc.june + a.june,
            july: acc.july + a.july,
            aug: acc.aug + a.aug,
            sept: acc.sept + a.sept,
            oct: acc.oct + a.oct,
            nov: acc.nov + a.nov,
            dec : acc.dec + a.dec,
        }));

        setDreRevenue(revenue.map((item: any) => ({
            categoryId: item.categoryId,
            name: item.name,
            type: item.type,
            
            jan: numberToString(item.jan),
            janPerc: item.jan === 0 || totalRev.jan === 0 ? numberToStringPerc(0) : numberToStringPerc(item.jan / totalRev.jan),
            
            feb: numberToString(item.feb),
            febPerc: item.feb === 0 || totalRev.feb === 0 ? numberToStringPerc(0) : numberToStringPerc(item.feb / totalRev.feb),
            
            mar: numberToString(item.mar),
            marPerc: item.mar === 0 || totalRev.mar === 0 ? numberToStringPerc(0) : numberToStringPerc(item.mar / totalRev.mar),

            apr: numberToString(item.apr),
            aprPerc: item.apr === 0 || totalRev.apr === 0 ? numberToStringPerc(0) : numberToStringPerc(item.apr / totalRev.apr),

            may: numberToString(item.may),
            mayPerc: item.may === 0 || totalRev.may === 0 ? numberToStringPerc(0) : numberToStringPerc(item.may / totalRev.may),

            june: numberToString(item.june),
            junePerc: item.june === 0 || totalRev.june === 0 ? numberToStringPerc(0) : numberToStringPerc(item.june / totalRev.june),

            july: numberToString(item.july),
            julyPerc: item.july === 0 || totalRev.july === 0 ? numberToStringPerc(0) : numberToStringPerc(item.july / totalRev.july),

            aug: numberToString(item.aug),
            augPerc: item.aug === 0 || totalRev.aug === 0 ? numberToStringPerc(0) : numberToStringPerc(item.aug / totalRev.aug),

            sept: numberToString(item.sept),
            septPerc: item.sept === 0 || totalRev.sept === 0 ? numberToStringPerc(0) : numberToStringPerc(item.sept / totalRev.sept),

            oct: numberToString(item.oct),
            octPerc: item.oct === 0 || totalRev.oct === 0 ? numberToStringPerc(0) : numberToStringPerc(item.oct / totalRev.oct),

            nov: numberToString(item.nov),
            novPerc: item.nov === 0 || totalRev.nov === 0 ? numberToStringPerc(0) : numberToStringPerc(item.nov / totalRev.nov),

            dec: numberToString(item.dec),
            decPerc: item.dec === 0 || totalRev.dec === 0 ? numberToStringPerc(0) : numberToStringPerc(item.dec / totalRev.dec),
        })));

        setDreExpense(expense.map((item: any) => ({
            categoryId: item.categoryId,
            name: item.name,
            type: item.type,
            
            jan: numberToString(item.jan),
            janPerc: item.jan === 0 || totalRev.jan === 0 ? numberToStringPerc(0) : numberToStringPerc(item.jan / totalRev.jan),
            
            feb: numberToString(item.feb),
            febPerc: item.feb === 0 || totalRev.feb === 0 ? numberToStringPerc(0) : numberToStringPerc(item.feb / totalRev.feb),
            
            mar: numberToString(item.mar),
            marPerc: item.mar === 0 || totalRev.mar === 0 ? numberToStringPerc(0) : numberToStringPerc(item.mar / totalRev.mar),

            apr: numberToString(item.apr),
            aprPerc: item.apr === 0 || totalRev.apr === 0 ? numberToStringPerc(0) : numberToStringPerc(item.apr / totalRev.apr),

            may: numberToString(item.may),
            mayPerc: item.may === 0 || totalRev.may === 0 ? numberToStringPerc(0) : numberToStringPerc(item.may / totalRev.may),

            june: numberToString(item.june),
            junePerc: item.june === 0 || totalRev.june === 0 ? numberToStringPerc(0) : numberToStringPerc(item.june / totalRev.june),

            july: numberToString(item.july),
            julyPerc: item.july === 0 || totalRev.july === 0 ? numberToStringPerc(0) : numberToStringPerc(item.july / totalRev.july),

            aug: numberToString(item.aug),
            augPerc: item.aug === 0 || totalRev.aug === 0 ? numberToStringPerc(0) : numberToStringPerc(item.aug / totalRev.aug),

            sept: numberToString(item.sept),
            septPerc: item.sept === 0 || totalRev.sept === 0 ? numberToStringPerc(0) : numberToStringPerc(item.sept / totalRev.sept),

            oct: numberToString(item.oct),
            octPerc: item.oct === 0 || totalRev.oct === 0 ? numberToStringPerc(0) : numberToStringPerc(item.oct / totalRev.oct),

            nov: numberToString(item.nov),
            novPerc: item.nov === 0 || totalRev.nov === 0 ? numberToStringPerc(0) : numberToStringPerc(item.nov / totalRev.nov),

            dec: numberToString(item.dec),
            decPerc: item.dec === 0 || totalRev.dec === 0 ? numberToStringPerc(0) : numberToStringPerc(item.dec / totalRev.dec),
        })));

        setSaldo({
            jan: numberToString(totalRev.jan - totalExp.jan),
            feb: numberToString(totalRev.feb - totalExp.feb),
            mar: numberToString(totalRev.mar - totalExp.mar),
            apr: numberToString(totalRev.apr - totalExp.apr),
            may: numberToString(totalRev.may - totalExp.may),
            june: numberToString(totalRev.june - totalExp.june),
            july: numberToString(totalRev.july - totalExp.july),
            aug: numberToString(totalRev.aug - totalExp.aug),
            sept: numberToString(totalRev.sept - totalExp.sept),
            oct: numberToString(totalRev.oct - totalExp.oct),
            nov: numberToString(totalRev.nov - totalExp.nov),
            dec: numberToString(totalRev.dec - totalExp.dec),
        });
        setTotalRevenue({
            jan: numberToString(totalRev.jan),
            feb: numberToString(totalRev.feb),
            mar: numberToString(totalRev.mar),
            apr: numberToString(totalRev.apr),
            may: numberToString(totalRev.may),
            june: numberToString(totalRev.june),
            july: numberToString(totalRev.july),
            aug: numberToString(totalRev.aug),
            sept: numberToString(totalRev.sept),
            oct: numberToString(totalRev.oct),
            nov: numberToString(totalRev.nov),
            dec: numberToString(totalRev.dec),
        });
        setTotalExpense({
            jan: numberToString(totalExp.jan),
            feb: numberToString(totalExp.feb),
            mar: numberToString(totalExp.mar),
            apr: numberToString(totalExp.apr),
            may: numberToString(totalExp.may),
            june: numberToString(totalExp.june),
            july: numberToString(totalExp.july),
            aug: numberToString(totalExp.aug),
            sept: numberToString(totalExp.sept),
            oct: numberToString(totalExp.oct),
            nov: numberToString(totalExp.nov),
            dec: numberToString(totalExp.dec),
        });
    }

    useEffect(() => {
        fetchDRE();
    }, [year]);

    return (
        <Template title="Demonstração do Resultado do Exercício" >
            <main className="sm:col-span-3">
                <div className="w-full h-full bg-white shadow-sm rounded-md p-3">
                    <table className="h-full block overflow-auto text-sm relative">
                        <thead>
                            <tr className="">
                                <th className="z-50 bg-white left-0 top-0 sticky">{year}</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Jan</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Fev</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Mar</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Abr</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Mai</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">jun</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">jul</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Ago</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Set</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Out</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Nov</th>
                                <th colSpan={2} className="text-lg bg-white top-0 sticky border-slate-300 border-x-2">Dez</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td className="text-green-500 font-bold text-lg p-3 left-0 sticky bg-white">Receitas</td>
                            </tr>
                            
                            {dreRevenue.map(lineTable)}

                            <tr className="rounded-md">
                                <td className="p-3 left-0 bg-white sticky">Total Receita</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.jan}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.feb}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.mar}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.apr}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.may}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.june}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.july}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.aug}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.sept}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.oct}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.nov}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-green-500 border-x-2">{totalRevenue.dec}</td>
                            </tr>

                            <tr>
                                <td className="text-red-500 font-bold text-lg p-3 left-0 sticky bg-white">Despesas</td>
                            </tr>

                            {dreExpense.map( lineTable )}

                            <tr className="rounded-md">
                                <td className="p-3 left-0 bg-white sticky">Total Despesas</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.jan}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.feb}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.mar}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.apr}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.may}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.june}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.july}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.aug}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.sept}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.oct}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.nov}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-red-500 border-x-2">{totalExpense.dec}</td>
                            </tr>

                            <tr className="rounded-md">
                                <td className="p-3 left-0 bg-white sticky">Saldo</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.jan}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.feb}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.mar}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.apr}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.may}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.june}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.july}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.aug}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.sept}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.oct}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.nov}</td>
                                <td colSpan={2} className="py-3 px-5 text-right border-slate-300 text-blue-500 border-x-2">{saldo.dec}</td>
                            </tr>
                        </tbody>
                    </table>

                </div>
            </main>
        </Template>
    );
}

const lineTable = (item : any) => (
    <tr key={item.categoryId} className="rounded-md hover:bg-slate-200">
        <td className="p-3 font-medium left-0 sticky bg-white whitespace-nowrap">{item.name}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.janPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.jan}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.febPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.feb}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.marPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.mar}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.aprPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.apr}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.mayPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.may}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.junePerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.june}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.julyPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.july}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.augPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.aug}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.septPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.sept}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.octPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.oct}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.novPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.nov}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-l-2">{item.decPerc}</td>
        <td className="py-3 px-5 text-right border-slate-300 border-r-2">{item.dec}</td>
    </tr>

);