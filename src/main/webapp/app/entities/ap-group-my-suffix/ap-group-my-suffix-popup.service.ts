import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { APGroupMySuffix } from './ap-group-my-suffix.model';
import { APGroupMySuffixService } from './ap-group-my-suffix.service';

@Injectable()
export class APGroupMySuffixPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private aPGroupService: APGroupMySuffixService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.aPGroupService.find(id)
                    .subscribe((aPGroupResponse: HttpResponse<APGroupMySuffix>) => {
                        const aPGroup: APGroupMySuffix = aPGroupResponse.body;
                        this.ngbModalRef = this.aPGroupModalRef(component, aPGroup);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.aPGroupModalRef(component, new APGroupMySuffix());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    aPGroupModalRef(component: Component, aPGroup: APGroupMySuffix): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.aPGroup = aPGroup;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
