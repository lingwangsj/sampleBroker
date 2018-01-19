/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { ZoneMySuffixDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix-delete-dialog.component';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.service';

describe('Component Tests', () => {

    describe('ZoneMySuffix Management Delete Component', () => {
        let comp: ZoneMySuffixDeleteDialogComponent;
        let fixture: ComponentFixture<ZoneMySuffixDeleteDialogComponent>;
        let service: ZoneMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [ZoneMySuffixDeleteDialogComponent],
                providers: [
                    ZoneMySuffixService
                ]
            })
            .overrideTemplate(ZoneMySuffixDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ZoneMySuffixDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ZoneMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
