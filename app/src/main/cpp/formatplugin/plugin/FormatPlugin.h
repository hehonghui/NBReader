//
// Created by 陈广祥 on 2019-09-18.
//

#ifndef NBREADER_FORMATPLUGIN_H
#define NBREADER_FORMATPLUGIN_H

#include <string>
#include <entity/book/Book.h>
#include <entity/bookmodel/BookModel.h>
#include "FormatType.h"

class FormatPlugin {
protected:
    FormatPlugin();

public:
    virtual ~FormatPlugin();
    /*写入书籍的元数据信息*/
    virtual bool readMetaInfo(Book &book) const = 0;
    /*写入 BookModel 信息*/
    virtual bool readModel(BookModel &bookModel) const = 0;

    virtual const FormatType supportType() const = 0;
};

inline FormatPlugin::FormatPlugin() {
}

inline FormatPlugin::~FormatPlugin() {
}


#endif //NBREADER_FORMATPLUGIN_H