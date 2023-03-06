import { ReactNode } from 'react';

type TemplateProps = {
    children: ReactNode;
    title?: String;
}

export function Template({ children, title } : TemplateProps) {
  return (
      <>
        { title && (
            <header className="shadow">
                <div className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
                    <h1 className="text-3xl font-bold text-gray-600">{ title }</h1>
                </div>
            </header>
        )}

        <main>
          <div className="max-w-7xl mx-auto py-6 px-3 sm:px-6 lg:px-6">
            { children }
          </div>
        </main>
      </>
  );
}